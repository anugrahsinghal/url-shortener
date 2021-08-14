package org.infracloud.url.shortener.services;

import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.infracloud.url.shortener.dto.ShortenUrlRequest;
import org.infracloud.url.shortener.entities.URLMapping;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;
import org.infracloud.url.shortener.excpetions.ShortLinkExpiredException;
import org.infracloud.url.shortener.excpetions.ShortLinkNotFoundException;
import org.infracloud.url.shortener.repository.URLMappingRepository;
import org.infracloud.url.shortener.utils.URLConvertor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class URLShortenerServiceImpl implements URLShortener {

	private final URLConvertor convertor;
	private final URLMappingRepository urlMappingRepository;
	private final ExpiryValidationStrategy expiryValidation;

	@Cacheable(value = "short_urls", key = "#request.longUrl")
	public String shorten(ShortenUrlRequest request) throws InvalidExpirationException {

		final Optional<URLMapping> existingURL = urlMappingRepository.findByLongUrl(request.getLongUrl());
		if (existingURL.isPresent()) {
			log.info("URL Already Present");
			return convertor.encode(existingURL.get().getId());
		} else {
			log.info("URL Not Present, Creating New Entry");
			URLMapping entity = saveURLMapping(request);
			return convertor.encode(entity.getId());
		}
	}

	private URLMapping saveURLMapping(ShortenUrlRequest request) throws InvalidExpirationException {
		final URLMapping url = new URLMapping(request.getLongUrl());

		if (request.getExpiry() != null) {
			expiryValidation.validate(request.getExpiry());
			url.setExpirationTime(request.getExpiry());
		}

		return urlMappingRepository.save(url);
	}

	@Cacheable(value = "long_urls", key = "#shortLink")
	public String resolveOriginalURL(String shortLink) throws ShortLinkNotFoundException, ShortLinkExpiredException {
		long id = convertor.decode(shortLink);

		log.info("Finding Long URL for {} by decoded Id {}", shortLink, id);

		URLMapping entity = urlMappingRepository.findById(id)
				.orElseThrow(
						() -> new ShortLinkNotFoundException("The URL does not exist in the system")
				);

		if (entity.getExpirationTime() != null && entity.getExpirationTime().isBefore(Instant.now())) {
			log.debug("URL expired {}", shortLink);
			urlMappingRepository.delete(entity);
			throw new ShortLinkExpiredException("The Short Link has expired!");
		}

		return entity.getLongUrl();
	}

}
