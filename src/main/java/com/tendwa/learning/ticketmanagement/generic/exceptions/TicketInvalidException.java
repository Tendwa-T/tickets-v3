package com.tendwa.learning.ticketmanagement.generic.exceptions;

public class TicketInvalidException extends RuntimeException {
    public TicketInvalidException(String message) {
        super(message);
    }
}
