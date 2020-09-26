package com.sand_corporation.rerofit_with_flask.api.utils;

public class APIError {
    private int statusCode;
    private String message;

    public APIError() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
