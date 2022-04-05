package it.example.crud.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class User extends UserPostRequest{

	@ApiModelProperty(value = "identifier of the user", name = "id", dataType = "Long", example = "1")
	private long id;

}
