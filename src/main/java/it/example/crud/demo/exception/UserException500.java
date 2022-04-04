
package it.example.crud.demo.exception;

import java.io.Serial;

public class UserException500 extends UserException {

	@Serial
	private static final long serialVersionUID = 7005175496151435863L;

	public UserException500(String message) {

		super(message, null);
	}

	public UserException500(String message, Throwable rootCause) {

		super(message, rootCause);
	}

}
