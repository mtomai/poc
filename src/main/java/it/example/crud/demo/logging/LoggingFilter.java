
package it.example.crud.demo.logging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

	private final String serviceName;
	private List<String> pathToExclude = Arrays.asList("/api-docs", "/swagger-resources/**", "/swagger-ui.html",
									"/webjars/**", "/csrf", "/");
	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final int logSize;

	public LoggingFilter(String serviceName, int logSize) {

		this.serviceName = serviceName;
		this.logSize = logSize;
	}

	public LoggingFilter(String serviceName) {

		this.serviceName = serviceName;
		this.logSize = 5120;
	}

	public void addPathToExclude(String... newPaths) {

		List<String> newList = new ArrayList<>(this.pathToExclude);
		newList.addAll(Arrays.asList(newPaths));
		this.pathToExclude = newList;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		return pathToExclude.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest _request, HttpServletResponse _response, FilterChain filterChain)
									throws ServletException, IOException {

		long startTime = System.currentTimeMillis();
		HttpServletRequest request = _request;
		HttpServletResponse response = _response;

		if (!(request instanceof ContentCachingRequestWrapper)) {
			request = new ContentCachingRequestWrapper(request);
		}
		if (!(response instanceof ContentCachingResponseWrapper)) {
			response = new ContentCachingResponseWrapper(response);
		}

		filterChain.doFilter(request, response);
		try {
			if ((!pathToExclude.contains(request.getRequestURI()))) {
				String reqLog = ">>> ".concat(Objects.requireNonNull(request.getMethod())).concat(" ")
												.concat(request.getRequestURI()).concat("||")
												+ "Headers : ".concat(getStringReqHeaders(request)).concat("||")
												+ "Request body : ".concat(getRequestPayload(request));
				log.info(reqLog);
			}
		} finally {
			if ((!pathToExclude.contains(request.getRequestURI()))) {
				if (!isAsyncStarted(request)) {
					String respLog = "<<< STATUS_CODE:".concat(String.valueOf(response.getStatus())).concat("||")
													+ "Headers : ".concat(getStringRespHeaders(response)).concat("||")
													+ "Response body : ".concat(getResponsePayload(response));
					log.info(respLog);

					long requestDuration = new Date().getTime() - startTime;
				}
			}
		}
	}

	private String getStringReqHeaders(HttpServletRequest request) {

		StringBuilder result = new StringBuilder();
		result.append("[");

		for (Iterator<String> it = Collections.list(request.getHeaderNames()).iterator(); it.hasNext(); ) {
			String headerName = it.next();

			result.append(headerName).append(": ").append(request.getHeader(headerName));
			if (it.hasNext()) {
				result.append(", ");
			}
		}
		result.append("]");
		return result.toString();
	}

	private String getStringRespHeaders(HttpServletResponse response) {

		StringBuilder result = new StringBuilder();
		result.append("[");
		for (Iterator<String> it = response.getHeaderNames().iterator(); it.hasNext(); ) {
			String headerName = it.next();

			result.append(headerName).append(": ").append(response.getHeader(headerName));
			if (it.hasNext()) {
				result.append(", ");
			}
		}
		result.append("]");
		return result.toString();
	}

	private String getRequestPayload(HttpServletRequest request) {

		String payload = "";
		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				int length = Math.min(buf.length, logSize);
				try {
					payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException ex) {
					log.error("Unsopported encoding exception while creating payload: ", ex);
				}
			}
		}
		return payload.replaceAll("(\r\n|\n\r|\r|\n)", "");
	}

	private String getResponsePayload(HttpServletResponse response) {

		String payload = "";
		ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
										ContentCachingResponseWrapper.class);

		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				int length = Math.min(buf.length, logSize);
				try {
					payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
					wrapper.copyBodyToResponse();
				} catch (UnsupportedEncodingException ex) {
					log.error("Unsopported encoding exception while creating payload: ", ex);
				} catch (IOException e) {
					log.error("IO Exception while parsing response payload: ", e);
				}
			}
		}
		return payload;
	}
}
