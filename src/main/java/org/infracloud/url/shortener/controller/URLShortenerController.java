package org.infracloud.url.shortener.controller;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.infracloud.url.shortener.dto.ShortenUrlRequest;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;
import org.infracloud.url.shortener.excpetions.ShortLinkExpiredException;
import org.infracloud.url.shortener.excpetions.ShortLinkNotFoundException;
import org.infracloud.url.shortener.services.URLShortener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Log4j2
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class URLShortenerController {

	private final URLShortener urlShortenerService;

	@Operation(description = "Supply Request with Valid URL to generate a short URL")
	@PostMapping("/shorten")
	public String shortenURL(@RequestBody @Valid ShortenUrlRequest request) throws InvalidExpirationException {
		log.info("request [{}]", request);
		return urlShortenerService.shorten(request);
	}

	@Operation(description = "Supply valid short URL and you are re-directed to the original long url")
	@GetMapping(value = "{shortLink}")
	public RedirectView resolveAndRedirect(@PathVariable String shortLink) throws ShortLinkNotFoundException, ShortLinkExpiredException {
		log.info("shortLink [{}]", shortLink);
		String originalURL = urlShortenerService.resolveOriginalURL(shortLink);

		return new RedirectView(originalURL);
	}

}
