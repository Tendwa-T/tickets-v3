package com.tendwa.learning.ticketmanagement.auth.dtos.role;

import lombok.Data;

@Data
public class AssignRoleRequest {
    private Long userId;
    private Long roleId;
}
