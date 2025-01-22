package com.poketube.Utils.Errors;

public class TooManyAttacks extends RuntimeException {
    public TooManyAttacks(String message) {
        super(message);
    }
}
