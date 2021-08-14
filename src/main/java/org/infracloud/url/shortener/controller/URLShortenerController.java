package org.infracloud.url.shortener.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class URLShortenerController {

	private final URLShortener urlShortenerService;

	@PostMapping("/shorten")
	public String shortenURL(@RequestBody @Valid ShortenUrlRequest request) throws InvalidExpirationException {
		return urlShortenerService.shorten(request);
	}

	@GetMapping(value = "{shortLink}")
	public RedirectView resolveAndRedirect(@PathVariable String shortLink) throws ShortLinkNotFoundException, ShortLinkExpiredException {
		String originalURL = urlShortenerService.resolveOriginalURL(shortLink);

		return new RedirectView(originalURL);
	}

}
