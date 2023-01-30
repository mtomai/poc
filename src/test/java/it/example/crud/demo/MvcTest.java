package it.example.crud.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.example.crud.demo.models.Gender;
import it.example.crud.demo.models.UserPostRequest;
import it.example.crud.demo.models.db.PermissionTable;
import it.example.crud.demo.models.db.UserTable;
import it.example.crud.demo.repository.PermissionRepository;
import it.example.crud.demo.repository.UserRepository;
import it.example.crud.demo.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//in classes indico qual è la classe con il main
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MvcTest {

    @Autowired
    private MockMvc mvc;

    //il mockbean serve per dire di simulare la repository e di non utilizzare quella vera
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PermissionRepository permissionRepository;

    //funziona
    @Test
    public void getUsers_200() throws Exception {
        List<UserTable> listaUsers = new ArrayList<UserTable>();
        listaUsers.add(new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981"));
        listaUsers.add(new UserTable(2l,"Giulio", "verde",39, "M","12-06-1981"));
        given(userRepository.findAll()).willReturn(listaUsers);
        mvc.perform(get("/user-service/users").header("Authorization","test")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName",is("Mario")));
    }

    //funziona
    @Test
    public void getUser_200() throws Exception {
        UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
      /*  List<UserTable> listaUsers = new ArrayList<UserTable>();
        listaUsers.add(userTable);
        listaUsers.add(new UserTable(2l,"Giulio", "verde",39, "M","12-06-1981"));*/

        given(userRepository.findById(anyLong())).willReturn(Optional.of(userTable));
        mvc.perform(get("/user-service/userById/1").header("Authorization","test"))
                .andExpect(status().isOk()); //.andExpect(jsonPath(""));
    }

    //funziona
    @Test
    public void getUser_404() throws Exception {
        UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
      /*  List<UserTable> listaUsers = new ArrayList<UserTable>();
        listaUsers.add(userTable);
        listaUsers.add(new UserTable(2l,"Giulio", "verde",39, "M","12-06-1981"));*/

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
        mvc.perform(get("/user-service/userById/1").header("Authorization","test"))
                .andExpect(status().is4xxClientError()); //.andExpect(jsonPath(""));
    }

    //funziona
    //aggiungere se sbagli user o password o il ruolo non va bene -> errore 401 ->fatto
    @Test
    public void testDeleteUser() throws Exception {
        List<PermissionTable> permissionTableList = new ArrayList<PermissionTable>();
        permissionTableList.add(new PermissionTable(1l, "utente1", "pw1", "admin"));
        permissionTableList.add(new PermissionTable(2l, "utente2", "pw2", "other"));
        doNothing().when(userRepository).deleteById(anyLong());
        //per farlo passare nel permission filter a fare i controlli
        given(permissionRepository.findAll()).willReturn(new ArrayList<>());
        mvc.perform(delete("/user-service/deleteUser/1").header("Authorization","test")
                        .header("Username","utente1").header("Password","pw1"))
                .andExpect(status().is4xxClientError());
        //test con la lista che ho inserito
        given(permissionRepository.findAll()).willReturn(permissionTableList);
        mvc.perform(delete("/user-service/deleteUser/1").header("Authorization","test")
                        .header("Username","utente1").header("Password","pw1"))
                .andExpect(status().isOk());
        mvc.perform(delete("/user-service/deleteUser/1").header("Authorization","test")
                        .header("Username","utente3").header("Password","pw2"))
                .andExpect(status().is4xxClientError());
        mvc.perform(delete("/user-service/deleteUser/1").header("Authorization","test")
                        .header("Username","utente2").header("Password","pw2"))
                .andExpect(status().is4xxClientError());
        mvc.perform(delete("/user-service/deleteUser/1").header("Authorization","test")
                        .header("Username","utente1").header("Password","pw2"))
                .andExpect(status().is4xxClientError());
    }

    // funziona
    @Test
    public void testNewUser() throws Exception {
        UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
        when(userRepository.save(any())).thenReturn(userTable);
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setFirstName("Mario");
        userPostRequest.setLastName("Verdi");
        userPostRequest.setAge(39);
        userPostRequest.setGender(Gender.M);
        userPostRequest.setDateOfBirth(new Date());
        userService.newUser(userPostRequest);

        mvc.perform(post("/user-service/newUser").content(asJsonString(userPostRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","test"))
                .andExpect(status().isOk());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //funziona
    @Test
    public void testUpdateAgeWithResult() throws Exception {
        //find by id
        UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userTable));

        mvc.perform(put("/user-service/updateEta/1/90").header("Authorization","test"))
                .andExpect(status().isOk());
    }

    //funziona
    @Test
    public void testUpdateAgeWithoutResult() throws Exception {
        //find by id
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(put("/user-service/updateEta/1/99").header("Authorization","test"))
                .andExpect(status().isOk());
    }

}
