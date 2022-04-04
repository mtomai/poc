package it.example.crud.demo.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.example.crud.demo.models.ErrorResponse;
import it.example.crud.demo.models.User;
import it.example.crud.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
	public List<User> getUsers (@ApiParam(name = "Authorization", type = "String", value = "authorization phrase", example = "Password1234", required = true)
																																@RequestHeader(value = "Authorization") String authorization) {

		log.debug("Try to get users");
		return userService.getUsers();

	}

}
