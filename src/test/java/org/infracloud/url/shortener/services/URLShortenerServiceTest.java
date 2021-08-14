package org.infracloud.url.shortener.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.time.Instant;
import java.util.Optional;
import org.infracloud.url.shortener.dto.ShortenUrlRequest;
import org.infracloud.url.shortener.entities.URLMapping;
import org.infracloud.url.shortener.excpetions.InvalidExpirationException;
import org.infracloud.url.shortener.excpetions.ShortLinkExpiredException;
import org.infracloud.url.shortener.excpetions.ShortLinkNotFoundException;
import org.infracloud.url.shortener.repository.URLMappingRepository;
import org.infracloud.url.shortener.utils.URLConvertor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class URLShortenerServiceTest {

	private URLConvertor convertor;
	private URLMappingRepository urlMappingRepository;
	private ExpiryValidationStrategy expiryValidation;
	private URLShortener urlShortener;
	private final String valid_LONG_URL = "https://www.google.com";
	private final String SHORT_URL = "shortURL";


	@BeforeEach
	void setUp() {
		convertor = mock(URLConvertor.class);
		urlMappingRepository = mock(URLMappingRepository.class);
		expiryValidation = mock(ExpiryValidationStrategy.class);
		urlShortener = new URLShortenerServiceImpl(
				convertor,
				urlMappingRepository,
				expiryValidation
		);
	}

	@Test
	void should_return_a_shortened_url_and_save_to_db() throws InvalidExpirationException {
		final long entityId = 1L;
		final URLMapping returned = mock(URLMapping.class);
		when(urlMappingRepository.findByLongUrl(valid_LONG_URL)).thenReturn(Optional.empty());
		when(urlMappingRepository.save(any(URLMapping.class))).thenReturn(returned);
		when(returned.getId()).thenReturn(entityId);
		when(convertor.encode(eq(entityId))).thenReturn(SHORT_URL);

		final String shortenedURL = urlShortener.shorten(new ShortenUrlRequest(valid_LONG_URL));

		assertEquals(SHORT_URL, shortenedURL);
		Mockito.verify(urlMappingRepository, times(1)).findByLongUrl(eq(valid_LONG_URL));
		Mockito.verify(urlMappingRepository, times(1)).save(any(URLMapping.class));
	}

	@Test
	void should_return_same_short_url_when_key_already_present_in_db() throws InvalidExpirationException {
		final long entityId = 1L;
		final URLMapping returned = mock(URLMapping.class);
		when(urlMappingRepository.findByLongUrl(valid_LONG_URL)).thenReturn(Optional.of(returned));
		when(returned.getId()).thenReturn(entityId);
		when(convertor.encode(eq(entityId))).thenReturn(SHORT_URL);

		final String shortenedURL = urlShortener.shorten(new ShortenUrlRequest(valid_LONG_URL));

		assertEquals(SHORT_URL, shortenedURL);
		Mockito.verify(urlMappingRepository, times(1)).findByLongUrl(eq(valid_LONG_URL));
		Mockito.verify(urlMappingRepository, times(0)).save(any(URLMapping.class));
	}

	@Test
	void should_not_validate_expiry_when_expiry_not_present() throws InvalidExpirationException {
		final long entityId = 1L;
		final URLMapping returned = mock(URLMapping.class);
		when(urlMappingRepository.findByLongUrl(valid_LONG_URL)).thenReturn(Optional.empty());
		when(urlMappingRepository.save(any(URLMapping.class))).thenReturn(returned);
		when(returned.getId()).thenReturn(entityId);
		when(convertor.encode(eq(entityId))).thenReturn(SHORT_URL);

		urlShortener.shorten(new ShortenUrlRequest(valid_LONG_URL));

		Mockito.verify(expiryValidation, times(0)).validate(any(Instant.class));
	}

	@Test
	void should_validate_expiry_when_expiry_present_and_not_throw_exception() throws InvalidExpirationException {
		final long entityId = 1L;
		final URLMapping returned = mock(URLMapping.class);
		when(urlMappingRepository.findByLongUrl(valid_LONG_URL)).thenReturn(Optional.empty());
		when(urlMappingRepository.save(any(URLMapping.class))).thenReturn(returned);
		when(returned.getId()).thenReturn(entityId);
		when(convertor.encode(eq(entityId))).thenReturn(SHORT_URL);
		doNothing().when(expiryValidation).validate(any(Instant.class));

		urlShortener.shorten(new ShortenUrlRequest(valid_LONG_URL, Instant.now()));

		Mockito.verify(expiryValidation, times(1)).validate(any(Instant.class));
	}

	@Test
	void should_validate_expiry_and_throw_exception_for_invalid_value() throws InvalidExpirationException {
		final long entityId = 1L;
		final URLMapping returned = mock(URLMapping.class);
		when(urlMappingRepository.findByLongUrl(valid_LONG_URL)).thenReturn(Optional.empty());
		doThrow(InvalidExpirationException.class).when(expiryValidation).validate(any(Instant.class));

		assertThrows(InvalidExpirationException.class,
				() -> urlShortener.shorten(new ShortenUrlRequest(valid_LONG_URL, Instant.now())));

		Mockito.verify(expiryValidation, times(1)).validate(any(Instant.class));
	}

	@Test
	void should_get_long_url_for_short_url_from_repo() throws ShortLinkNotFoundException, ShortLinkExpiredException {
		final long entityId = 1L;
		when(convertor.decode(SHORT_URL)).thenReturn(entityId);
		final URLMapping urlMappingMock = mock(URLMapping.class);
		when(urlMappingRepository.findById(entityId)).thenReturn(Optional.of(urlMappingMock));
		when(urlMappingMock.getExpirationTime()).thenReturn(null);
		when(urlMappingMock.getLongUrl()).thenReturn(valid_LONG_URL);

		final String originalURL = urlShortener.resolveOriginalURL(SHORT_URL);
		assertEquals(valid_LONG_URL, originalURL);
		verify(convertor, times(1)).decode(eq(SHORT_URL));
	}

	@Test
	void should_throw_exception_when_short_link_not_present() {
		final long entityId = 1L;
		when(convertor.decode(SHORT_URL)).thenReturn(entityId);
		when(urlMappingRepository.findById(entityId)).thenReturn(Optional.empty());

		assertThrows(ShortLinkNotFoundException.class, () -> urlShortener.resolveOriginalURL(SHORT_URL));
	}

	@Test
	void should_throw_exception_and_delete_link_when_short_link_expired() {
		final long entityId = 1L;
		when(convertor.decode(SHORT_URL)).thenReturn(entityId);
		final URLMapping urlMappingMock = mock(URLMapping.class);
		when(urlMappingRepository.findById(entityId)).thenReturn(Optional.of(urlMappingMock));
		when(urlMappingMock.getExpirationTime()).thenReturn(Instant.MIN);
		doNothing().when(urlMappingRepository).delete(eq(urlMappingMock));

		assertThrows(ShortLinkExpiredException.class, () -> urlShortener.resolveOriginalURL(SHORT_URL));
		verify(urlMappingRepository, times(1)).delete(eq(urlMappingMock));
	}

}