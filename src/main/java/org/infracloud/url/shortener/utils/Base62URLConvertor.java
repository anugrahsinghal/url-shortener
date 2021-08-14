package org.infracloud.url.shortener.utils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class Base62URLConvertor implements URLConvertor {

	private static final String allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final char[] map = allowedString.toCharArray();
	private final int base = map.length;

	@Override
	public String encode(long n) {
		StringBuilder shortUrl = new StringBuilder();

		// Convert given integer id to a base 62 number
		while (n > 0) {
			// use map to store actual character in short url
			shortUrl.append(map[(int) (n % base)]);
			n = n / base;
		}

		// Reverse shortURL to complete base conversion
		return shortUrl.reverse().toString();
	}

	@Override
	public long decode(String shortURL) {
		long id = 0; // initialize result

		// A simple base conversion logic
		for (int i = 0; i < shortURL.length(); i++) {
			if ('a' <= shortURL.charAt(i) && shortURL.charAt(i) <= 'z') {
				id = id * base + shortURL.charAt(i) - 'a';
			}
			if ('A' <= shortURL.charAt(i) && shortURL.charAt(i) <= 'Z') {
				id = id * base + shortURL.charAt(i) - 'A' + 26;
			}
			if ('0' <= shortURL.charAt(i) && shortURL.charAt(i) <= '9') {
				id = id * base + shortURL.charAt(i) - '0' + 52;
			}
		}
		return id;
	}

}
