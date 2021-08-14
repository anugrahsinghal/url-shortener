package org.infracloud.url.shortener.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class ExceptionWrapper {
	private final String message;
}
