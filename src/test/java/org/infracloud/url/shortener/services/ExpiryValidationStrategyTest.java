package org.infracloud.url.shortener.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpiryValidationStrategyTest {

	ExpiryValidationStrategy expiryValidationStrategy;

	@BeforeEach
	void setUp() {
		expiryValidationStrategy = new FiveMinuteExpiryValidationStrategy();
	}

	@Test
	void should_throw_exception_when_expiry_before_5_minutes() {
		Assertions.assertThrows(InvalidExpirationException.class, () -> expiryValidationStrategy.validate(Instant.now()));
	}

	@Test
	void should_throw_no_exception_when_expiry_after_5_minutes() {
		Assertions.assertDoesNotThrow(() -> expiryValidationStrategy.validate(Instant.now().plus(10, ChronoUnit.MINUTES)));
	}

}