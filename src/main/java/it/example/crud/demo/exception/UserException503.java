
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException503 extends UserException {

	@Serial
	private static final long serialVersionUID = 1004581907001625340L;

	public UserException503(String message) {

		super(message, null);
	}

	public UserException503(String message, Throwable rootCause) {

		super(message, rootCause);
	}

}
