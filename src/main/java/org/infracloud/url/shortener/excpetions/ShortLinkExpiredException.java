package org.infracloud.url.shortener.excpetions;

public class ShortLinkExpiredException extends Exception {
	public ShortLinkExpiredException(String s) {
		super(s);
	}
}
