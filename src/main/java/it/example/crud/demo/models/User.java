package it.example.crud.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class User {

	@ApiModelProperty(value = "identifier of the user", name = "id", dataType = "Long", example = "1")
	private long id;

	@ApiModelProperty(value = "first name of the user", name = "firstName", dataType = "String", example = "Mario")
	private String firstName;

	@ApiModelProperty(value = "last name of the user", name = "lastName", dataType = "String", example = "Rossi")
	private String lastName;

	@ApiModelProperty(value = "age of the user", name = "age", dataType = "Integer", example = "43")
	private int age;

	@ApiModelProperty(value = "gender of the user", name = "gender", dataType = "it.example.crud.demo.models.Gender", example = "M", allowableValues = "F, M")
	private Gender gender;

	@ApiModelProperty(value = "date of birth of the user", name = "dateOfBirth", example = "20-11-1979")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY", timezone = "Europe/Berlin")
	private Date dateOfBirth;
}
