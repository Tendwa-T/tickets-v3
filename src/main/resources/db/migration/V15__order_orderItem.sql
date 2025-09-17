create table orders
(
    id             BIGINT auto_increment
        primary key,
    customer       Bigint                                                                        not null,
    payment_status ENUM ('PENDING', 'PAID', 'FAILED', 'CANCELLED') default 'PENDING'             not null,
    created_at     datetime                                        default (CURRENT_TIMESTAMP()) not null,
    total_price    decimal(10, 2)                                  default 0.0                   not null,
    constraint orders_users_id_fk
        foreign key (customer) references users (id)
);

create table order_items
(
    id          BIGINT auto_increment
        primary key,
    order_id    BIGINT         not null,
    ticket_id   bigint         not null,
    unit_price  decimal(10, 2) not null,
    quantity    int default 1  not null,
    total_price decimal(10, 2) not null,
    constraint order_items___fk
        foreign key (ticket_id) references tickets (id),
    constraint order_items_orders_id_fk
        foreign key (order_id) references orders (id)
);

