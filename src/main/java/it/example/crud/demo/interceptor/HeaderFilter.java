package it.example.crud.demo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.example.crud.demo.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


@Component
@Order(1)
public class HeaderFilter implements Filter {

	@Value("${it.example.phrase}")
	private String phrase;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

		Optional<String> authorization = Optional.ofNullable(httpRequest.getHeader("Authorization"));

		if (authorization.isPresent() && !authorization.get().equals(phrase)) {
			ErrorResponse errorResponse = new ErrorResponse("AUTH-00", HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
											"Invalid Authorization header!");

			byte[] responseToSend = restResponseBytes(errorResponse);
			((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
			((HttpServletResponse) servletResponse).setStatus(401);
			servletResponse.getOutputStream().write(responseToSend);
			return;
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}

	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
		return serialized.getBytes();
	}
}