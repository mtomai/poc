package it.example.crud.demo.models.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserTable {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String lastName;

	private int age;

	private String gender;

	private String dateOfBirth;
}