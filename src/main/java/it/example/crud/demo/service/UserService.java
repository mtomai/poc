package it.example.crud.demo.service;

import it.example.crud.demo.models.Gender;
import it.example.crud.demo.models.User;
import it.example.crud.demo.models.UserPostRequest;
import it.example.crud.demo.models.db.UserTable;
import it.example.crud.demo.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

	private final static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private UserRepository userRepository;

	public List<User> getUsers() {

		log.debug("Getting users from DB");
		return userRepository.findAll().stream().map(UserService::mapTable).collect(Collectors.toList());

	}

	//ricerca per id
	public User getUserById(Long id){
		log.debug("Getting user by id");
		Optional<UserTable> byId = userRepository.findById(id);
		if(byId.isPresent()){
			return mapTable(byId.get());
		}
		return null;
	}

	//cancella per id
	public void deleteById(Long id){
		log.debug("Delete user by id");
		userRepository.deleteById(id);
	}

	//nuovo user
	public void newUser(UserPostRequest user){
		log.debug("Insert user");
		userRepository.save(mapUser(user));
	}

	//update user ->@Transactional è una annotation che serve per gli update
	@Transactional
	public void updateEta(Long id, int eta){
		log.debug("Update age");
		UserTable u= new UserTable();
		Optional<UserTable> byId = userRepository.findById(id);
		if(byId.isPresent()){
			//BeanUtils.copyProperties(byId, u);
			u.setAge(eta);
			userRepository.save(u);
		}
	}

	@SneakyThrows
	public static User mapTable(UserTable userTable) {
		User user = new User();
		user.setId(userTable.getId());
		user.setFirstName(userTable.getName());
		user.setLastName(userTable.getLastName());
		user.setAge(userTable.getAge());
		user.setGender(Gender.valueOf(userTable.getGender()));
		user.setDateOfBirth(formatter.parse(userTable.getDateOfBirth()));
		return user;
	}
/*	@SneakyThrows
	private static UserTable mapUser(User user) {
		UserTable userTable = new UserTable();
		userTable.setId(user.getId());
		userTable.setName(user.getFirstName());
		userTable.setLastName(user.getLastName());
		userTable.setAge(user.getAge());
		userTable.setGender(user.getGender().name());
		userTable.setDateOfBirth(formatter.format(user.getDateOfBirth()));
		return userTable;
	}*/

	@SneakyThrows
	private static UserTable mapUser(UserPostRequest user) {
		UserTable userTable = new UserTable();
		userTable.setName(user.getFirstName());
		userTable.setLastName(user.getLastName());
		userTable.setAge(user.getAge());
		userTable.setGender(user.getGender().name());
		userTable.setDateOfBirth(formatter.format(user.getDateOfBirth()));
		return userTable;
	}
}
