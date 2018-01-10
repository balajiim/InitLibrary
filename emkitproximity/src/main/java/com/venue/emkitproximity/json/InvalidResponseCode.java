package com.venue.emkitproximity.json;

@SuppressWarnings("serial")
public class InvalidResponseCode extends Exception {

	InvalidResponseCode(int code) {
		super("Invalid Response code " + code);
	}

}