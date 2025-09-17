create table tickets
(
    id             BIGINT auto_increment
        primary key,
    ticket_type_id BIGINT                                                                         not null,
    user_id        BIGINT                                                                         not null,
    order_id       BIGINT                                                                         null,
    status         ENUM ('ACTIVE', 'CANCELLED', 'REFUNDED', 'USED') default 'ACTIVE'              null,
    issue_at       datetime                                         default (CURRENT_TIMESTAMP()) not null,
    constraint tickets_ticket_types_id_fk
        foreign key (ticket_type_id) references ticket_types (id),
    constraint tickets_users_id_fk
        foreign key (user_id) references users (id)
);

