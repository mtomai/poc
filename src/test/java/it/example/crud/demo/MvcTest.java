package it.example.crud.demo;

import it.example.crud.demo.models.db.UserTable;
import it.example.crud.demo.repository.UserRepository;
import it.example.crud.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MvcTest {

    //a cosa serve?
    @Autowired
    private MockMvc mvc;

    // perchè uno è autowired e uno mockbean spiega differenza
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;


    @Test
    public void getUsers_200() throws Exception {
        List<UserTable> listaUsers = new ArrayList<UserTable>();
        listaUsers.add(new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981"));
        listaUsers.add(new UserTable(2l,"Giulio", "verde",39, "M","12-06-1981"));
        given(userRepository.findAll()).willReturn(listaUsers);
        mvc.perform(get("/user-service/users").header("Authorization","test")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName",is("Mario")));
    }

    //testa le restanti api
}
