create table carts
(
    id           binary(16) default (uuid_to_bin(uuid())) not null
        primary key,
    date_created date       default (curdate())           not null
);

create table cart_items
(
    id         bigint auto_increment
        primary key,
    cart_id    binary(16)    not null,
    ticket_id bigint        not null,
    quantity   int default 1 null,
    constraint cart_item_cart_product_unique
        unique (cart_id, ticket_id),
    constraint cart_items_carts_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint cart_items_tickets_id_fk
        foreign key (ticket_id) references tickets (id)
            on delete cascade
);

