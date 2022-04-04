
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException400 extends UserException {

	@Serial
	private static final long serialVersionUID = -3321857498962363154L;

	public UserException400(String message) {

		super(message, null);
	}

	public UserException400(String message, Throwable rootCause) {

		super(message, rootCause);
	}

}
