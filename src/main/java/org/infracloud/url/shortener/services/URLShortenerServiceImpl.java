package org.infracloud.url.shortener.services;

import lombok.RequiredArgsConstructor;
import org.infracloud.url.shortener.dto.ShortenUrlRequest;
import org.infracloud.url.shortener.entities.URLMapping;
import org.infracloud.url.shortener.excpetions.ShortLinkNotFoundException;
import org.infracloud.url.shortener.repository.URLMappingRepository;
import org.infracloud.url.shortener.utils.URLConvertor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class URLShortenerServiceImpl implements URLShortener {

	private final URLConvertor convertor;
	private final URLMappingRepository urlMappingRepository;

	public String shorten(ShortenUrlRequest request) {
		final URLMapping url = new URLMapping(
				request.getLongUrl()
		);

		URLMapping entity = urlMappingRepository.save(url);

		return convertor.encode(entity.getId());
	}

	public String resolveOriginalURL(String shortUrl) throws ShortLinkNotFoundException {
		long id = convertor.decode(shortUrl);
		URLMapping entity = urlMappingRepository.findById(id)
				.orElseThrow(
						() -> new ShortLinkNotFoundException("The URL does not exist in the system")
				);

		return entity.getLongUrl();
	}
}
