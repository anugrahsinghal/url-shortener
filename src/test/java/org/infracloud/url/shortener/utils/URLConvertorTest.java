package org.infracloud.url.shortener.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class URLConvertorTest {

	private URLConvertor urlConvertor;

	@BeforeEach
	void setUp() {
		urlConvertor = new Base62URLConvertor();
	}

	@Test
	public void should_encode_less_than_62() {
		assertEquals("P", urlConvertor.encode(41));
	}

	@Test
	public void should_encode_more_than_62() {
		assertEquals("g2gz", urlConvertor.encode(1637941));
	}

	@Test
	public void should_be_able_to_decode_single_character() {
		assertEquals(13, urlConvertor.decode("n"));
	}

	@Test
	public void should_be_able_to_decode_multiple_character() {
		assertEquals(384407371, urlConvertor.decode("Aa543"));
	}

}