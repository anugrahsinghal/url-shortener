package org.infracloud.url.shortener.controller;

import lombok.extern.log4j.Log4j2;
import org.infracloud.url.shortener.dto.ExceptionWrapper;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;
import org.infracloud.url.shortener.excpetions.ShortLinkNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class SpringExceptionHandler {

	@ExceptionHandler(value = {ShortLinkNotFoundException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseEntity<ExceptionWrapper> resourceNotFoundException(ShortLinkNotFoundException ex) {
		return getResponse(ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler( {InvalidExpirationException.class, Exception.class,})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionWrapper> handleBadRequests(Exception ex) {
		return getResponse(ex, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ExceptionWrapper> getResponse(Exception ex, HttpStatus status) {
		log.error("Exception ", ex);
		return new ResponseEntity<>(getFormattedData(ex), status);
	}

	private ExceptionWrapper getFormattedData(Exception e) {
		String message;
		if (e.getCause() != null) {
			message = e.getCause().toString() + " " + e.getMessage();
		} else {
			message = e.getMessage();
		}
		return new ExceptionWrapper(message);

	}
}