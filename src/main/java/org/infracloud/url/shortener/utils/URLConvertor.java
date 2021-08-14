package org.infracloud.url.shortener.utils;

public interface URLConvertor {

	long decode(String shortURL);

	String encode(long n);

}
