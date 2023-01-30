package it.example.crud.demo;

import it.example.crud.demo.models.Gender;
import it.example.crud.demo.models.UserPostRequest;
import it.example.crud.demo.models.db.UserTable;
import it.example.crud.demo.repository.UserRepository;
import it.example.crud.demo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
//in classes indico qual è la classe con il main
@SpringBootTest(classes = Application.class)
//@AutoConfigureMockMvc


class ApplicationTests {


	//a cosa serve?
	@Autowired
	private MockMvc mvc;

	//il mockbean serve per dire di simulare la repository e di non utilizzare quella vera
	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository;

	@Test
	public void testGetUsers() {
		List<UserTable> listaUsers = new ArrayList<UserTable>();
		listaUsers.add(new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981"));
		listaUsers.add(new UserTable(2l,"Giulio", "verde",39, "M","12-06-1981"));
		given(userRepository.findAll()).willReturn(listaUsers);
		Assertions.assertEquals(2, userService.getUsers().size());
	}

	@Test
	public void testGetUser200() {
		UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
		given(userRepository.findById(anyLong())).willReturn(Optional.of(userTable));
		Assertions.assertEquals("Mario", userService.getUserById(1l).getFirstName());
	}

	@Test
	public void testGetUser500() {
		UserTable userTable= new UserTable(1l,"Mario", "verdi",39, null,"12-06-1981");
		given(userRepository.findById(anyLong())).willReturn(Optional.of(userTable));
		Assertions.assertThrows(Exception.class,()->{userService.getUserById(1l);});
	}

	@Test
	public void testDeleteUser() {
		doNothing().when(userRepository).deleteById(anyLong());
	}

	@Test
	public void testNewUser() {
		UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
		when(userRepository.save(any())).thenReturn(userTable);
		UserPostRequest userPostRequest = new UserPostRequest();
		userPostRequest.setFirstName("Mario");
		userPostRequest.setLastName("Verdi");
		userPostRequest.setAge(39);
		userPostRequest.setGender(Gender.M);
		userPostRequest.setDateOfBirth(new Date());
		userService.newUser(userPostRequest);
		//verifico che la riga inserita corrisponda alla request ricevuta: devono avere gli stessi valori
		Assertions.assertEquals(userPostRequest.getFirstName(), userTable.getName());
	}

	@Test
	public void testUpdateAgeWithResult() {
		//find by id
		UserTable userTable= new UserTable(1l,"Mario", "verdi",39, "M","12-06-1981");
		given(userRepository.findById(anyLong())).willReturn(Optional.of(userTable));
		Assertions.assertEquals("Mario", userService.getUserById(1l).getFirstName());

		//se è presente cambio l'età
		userTable.setAge(44);

		//istruzioni della save
		when(userRepository.save(any())).thenReturn(userTable);
		userService.updateEta(1l, 44);

		//assertEquals
		Assertions.assertEquals(44,userTable.getAge());
	}

	@Test
	public void testUpdateAgeWithoutResult() {
		//find by id
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());
		//chiamo il metodo dello strato di servizio
		userService.updateEta(1l, 44);
	}


}
