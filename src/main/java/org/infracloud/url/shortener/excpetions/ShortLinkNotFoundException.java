package org.infracloud.url.shortener.excpetions;

public class ShortLinkNotFoundException extends Exception {
	public ShortLinkNotFoundException(String s) {
		super(s);
	}
}
