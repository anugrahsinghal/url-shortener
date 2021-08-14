package org.infracloud.url.shortener.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;
import org.springframework.stereotype.Component;

@Component
public class FiveMinuteExpiryValidationStrategy implements ExpiryValidationStrategy {

	@Override
	public void validate(Instant expiration) throws InvalidExpirationException {
		if(expiration.isBefore(Instant.now().plus(5, ChronoUnit.MINUTES))) {
			throw new InvalidExpirationException("Minimum expiration time is 5 Minutes");
		}
	}

}
