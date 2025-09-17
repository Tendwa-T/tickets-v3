package com.tendwa.learning.ticketmanagement.event.dtos.event;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.event.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventAuthValidation {
    private Event event;
    private User auth;
}
