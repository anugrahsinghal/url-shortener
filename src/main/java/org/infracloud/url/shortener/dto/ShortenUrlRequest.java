package org.infracloud.url.shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request Object for Shortening")
public class ShortenUrlRequest {

	@URL
	@Schema(required = true, description = "URL To Shorten")
	private String longUrl;

	@Schema(description = "Expiration datetime of url, can be supplied as a timestamp. "
	                      + "Expiration should be a minimum of 5 minutes from the current time",
			example = "1628973600 represents 2021-08-26T10:26:40Z")
	private Instant expiry;

	public ShortenUrlRequest(String longUrl) {
		this.longUrl = longUrl;
	}

}
