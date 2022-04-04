
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException extends Exception {

	@Serial
	private static final long serialVersionUID = 7342144059194695900L;
	private final String message;
	private final Throwable rootCause;

	public UserException(String message) {

		super(message);
		this.message = message;
		this.rootCause = null;
	}

	public UserException(String message, Throwable rootCause) {

		super(message, rootCause);
		this.message = message;
		this.rootCause = rootCause;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	public String getExceptionMessage() {

		return message;
	}

	public Throwable getRootCause() {

		return rootCause;
	}

}
