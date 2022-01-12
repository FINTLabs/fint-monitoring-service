package no.fintlabs.authentication;

public class FetchTokenException extends RuntimeException {
    public FetchTokenException() {
    }

    public FetchTokenException(String message) {
        super(message);
    }
}
