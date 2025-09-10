package com.tendwa.learning.ticketmanagement.generic.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String email) {

        super(String.format("User with %s already exists",email));
    }
}
