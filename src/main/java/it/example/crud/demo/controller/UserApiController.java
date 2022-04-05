package it.example.crud.demo.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.example.crud.demo.models.ErrorResponse;
import it.example.crud.demo.models.User;
import it.example.crud.demo.models.UserPostRequest;
import it.example.crud.demo.permission.Permission;
import it.example.crud.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "User")
public class UserApiController {


	@Autowired
	private UserService userService;

	@ApiOperation(value = "Get Users", nickname = "getUsers", notes = "This service return all users", tags = {"User"})
	@ApiResponses(value = {
									@ApiResponse(code = 200, message = "OK", response = User.class, responseContainer = "List"),
									@ApiResponse(code = 400, message = "Wrong Input", response = ErrorResponse.class),
									@ApiResponse(code = 500, message = "Something went wrong", response = ErrorResponse.class)
	})

	@GetMapping(value = "/user-service/users")
	public List<User> getContracts(@ApiParam(name = "Authorization", type = "String", value = "authorization phrase", example = "Password1234", required = true)
																																@RequestHeader(value = "Authorization") String authorization) {

		log.debug("Try to get users");
		return userService.getUsers();

	}

	@GetMapping(value = "user-service/userById/{id}")
	public ResponseEntity<User> getUserById(@ApiParam(name = "Authorization", type = "String", value = "authorization phrase", example = "Password1234", required = true) @RequestHeader(value = "Authorization") String authorization, @PathVariable Long id) {
		log.debug("Try to get user");
		User risultato = userService.getUserById(id);
		if (risultato!=null){
			return new ResponseEntity<>(risultato, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Permission(role="admin")
	@DeleteMapping(value = "user-service/deleteUser/{id}")
	public void deleteUserById(@ApiParam(name = "username", required = true) @RequestHeader(value = "username") String username, @ApiParam(name = "password", required = true) @RequestHeader(value = "password") String password, @PathVariable Long id) {
		log.debug("Try to delete user");
		userService.deleteById(id);
	}

	@PostMapping(value = "user-service/newUser")
	public void newUser(@ApiParam(name = "Authorization", type = "String", value = "authorization phrase", example = "Password1234", required = true) @RequestHeader(value = "Authorization") String authorization, @RequestBody UserPostRequest user) {
		log.debug("Try to add user");
		userService.newUser(user);
	}

	@PutMapping(value="user-service/updateEta/{id}/{eta}")
	public void updateEta(@ApiParam(name = "Authorization", type = "String", value = "authorization phrase", example = "Password1234", required = true) @RequestHeader(value = "Authorization") String authorization, @PathVariable Long id, @PathVariable int eta) {
		log.debug("Try to update");
		userService.updateEta(id,eta);
	}

}
