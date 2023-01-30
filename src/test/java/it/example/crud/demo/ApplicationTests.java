package it.example.crud.demo;

import it.example.crud.demo.models.db.UserTable;
import it.example.crud.demo.repository.UserRepository;
import it.example.crud.demo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
//in classes indico qual è la classe con il main
@SpringBootTest(classes = Application.class)
//@AutoConfigureMockMvc


class ApplicationTests {

	//il mockbean a cosa serve?
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

//testa le restanti api
}
