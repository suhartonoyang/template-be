package com.example.template.be.constant;

public class Constants {
	public static final String SIGN_UP_URLS = "template-be/api/users/**";
	public static final String H2_URL = "h2-console/**";
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 3_600_000; // 30seconds 1000 = 1 second
}
