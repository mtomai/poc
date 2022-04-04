
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException403 extends UserException {

	@Serial
	private static final long serialVersionUID = 1645583971713700652L;

	public UserException403(String message) {

		super(message, null);
	}

	public UserException403(String message, Throwable rootCause) {

		super(message, rootCause);
	}

}
