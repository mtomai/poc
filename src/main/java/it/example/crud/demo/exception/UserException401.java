
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException401 extends UserException {

	@Serial
	private static final long serialVersionUID = 3238291598493152986L;

	public UserException401(String message) {

		super(message, null);
	}

	public UserException401(String message, Throwable rootCause) {

		super(message, rootCause);
	}

}
