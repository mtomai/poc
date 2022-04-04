package it.example.crud.demo.controller;

import it.example.crud.demo.exception.UserException401;
import it.example.crud.demo.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionController {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse genericErrorHandler(HttpServletRequest req, Exception e) {

		log.error("", e);
		return new ErrorResponse("ERR-00", HttpStatus.INTERNAL_SERVER_ERROR, "Generic Error!",
										"A generic error occurred, please contact support");
	}

	@ExceptionHandler(UserException401.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse genericErrorHandler(HttpServletRequest req, UserException401 e) {

		log.error("", e);
		return new ErrorResponse("AUTH-00", HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
										e.getExceptionMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse validationErrorHandler(HttpServletRequest req, Exception e) {

		log.error("", e);
		return new ErrorResponse("REQ-00", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
										"Invalid JSON format.");
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse httpMediaTypeNotSupportedException(HttpServletRequest req, Exception e) {

		log.error("", e);
		return new ErrorResponse("REQ-01", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
										"Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse messageNotReadableException(HttpServletRequest req, Exception e) {

		log.error("", e);
		return new ErrorResponse("REQ-02", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
										"Unexpected end-of-input: expected close marker for Object");
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse httpMediaTypeNotAcceptableException(HttpServletRequest req, Exception e) {

		log.error("", e);
		return new ErrorResponse("REQ-03", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
										"Could not find acceptable representation");
	}

}
