package study.springjpa.domain.exception;

public class NotEnoughStockException extends RuntimeException {

	public NotEnoughStockException() {
		super();
	}

	public NotEnoughStockException(final String message) {
		super(message);
	}

	public NotEnoughStockException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotEnoughStockException(final Throwable cause) {
		super(cause);
	}

	protected NotEnoughStockException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
