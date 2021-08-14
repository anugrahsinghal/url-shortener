package org.infracloud.url.shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@ToString
@Getter
@Schema(description = "Request Object for Shortening")
public class ShortenUrlRequest {

	@URL
    @Schema(required = true, description = "URL To Shorten")
    private String longUrl;

	@Schema(description = "Expiration datetime of url")
	private Instant expiry;

}
