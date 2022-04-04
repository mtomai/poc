package it.example.crud.demo.service;

import it.example.crud.demo.models.Gender;
import it.example.crud.demo.models.User;
import it.example.crud.demo.models.db.UserTable;
import it.example.crud.demo.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

	private final static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private UserRepository userRepository;

	public List<User> getUsers() {

		log.debug("Getting users from DB");
		return userRepository.findAll().stream()
										.map(UserService::mapTable).collect(Collectors.toList());

	}

	@SneakyThrows
	private static User mapTable(UserTable userTable) {
		User user = new User();
		user.setId(userTable.getId());
		user.setFirstName(userTable.getName());
		user.setLastName(userTable.getLastName());
		user.setAge(userTable.getAge());
		user.setGender(Gender.valueOf(userTable.getGender()));
		user.setDateOfBirth(formatter.parse(userTable.getDateOfBirth()));
		return user;
	}
}
