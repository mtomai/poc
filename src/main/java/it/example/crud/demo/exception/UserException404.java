
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException404 extends UserException {

	@Serial
	private static final long serialVersionUID = -8233106341483517746L;

	public UserException404(String message) {

		super(message, null);
	}

	public UserException404(String message, Throwable rootCause) {

		super(message, rootCause);
	}

}
