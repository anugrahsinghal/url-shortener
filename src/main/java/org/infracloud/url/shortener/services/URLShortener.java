package org.infracloud.url.shortener.services;

import org.infracloud.url.shortener.dto.ShortenUrlRequest;
import org.infracloud.url.shortener.excpetions.ShortLinkNotFoundException;

public interface URLShortener {
	String shorten(ShortenUrlRequest request);

	String resolveOriginalURL(String shortUrl) throws ShortLinkNotFoundException;

}
