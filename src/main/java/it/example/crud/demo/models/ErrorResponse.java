package it.example.crud.demo.models;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ErrorResponse {


	@JsonProperty("code")
	private String code;

	@JsonProperty("status")
	private String status;

	@JsonProperty("title")
	private String title;

	@JsonProperty("detail")
	private String detail;

	@JsonProperty("timestamp")
	private String timestamp;


	public ErrorResponse(String code, HttpStatus status, String title, String detail) {
		this(code, String.valueOf(status.value()), title, detail, Instant.now().toString());
	}
}
