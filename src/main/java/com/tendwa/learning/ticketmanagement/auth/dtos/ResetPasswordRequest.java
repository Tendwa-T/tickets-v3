package com.tendwa.learning.ticketmanagement.auth.dtos;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private  String resetToken;
    private  String newPassword;
}
