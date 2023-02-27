package com.khopan.winimation.exception;

public class IllegalFieldFormatException extends RuntimeException {
	private static final long serialVersionUID = 4827235713411422402L;

	public IllegalFieldFormatException() {
		super();
	}

	public IllegalFieldFormatException(String message) {
		super(message);
	}

	public IllegalFieldFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalFieldFormatException(Throwable cause) {
		super(cause);
	}

	protected IllegalFieldFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
