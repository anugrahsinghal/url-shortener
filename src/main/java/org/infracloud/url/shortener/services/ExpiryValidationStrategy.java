package org.infracloud.url.shortener.services;

import java.time.Instant;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;

public interface ExpiryValidationStrategy {

	void validate(Instant expiration) throws InvalidExpirationException;

}
