package com.loukou.jconsul.client;

public class JConsulException extends RuntimeException {

    private static final long serialVersionUID = 8818123684507074968L;

    public JConsulException(String message) {
        super(message);
    }

    public JConsulException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JConsulException(Throwable cause) {
        super(cause);
    }

}
