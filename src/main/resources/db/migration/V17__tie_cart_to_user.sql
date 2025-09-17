alter table carts
    add customer_id BIGINT default 0 not null after id;

alter table carts
    add constraint carts_pk
        unique (customer_id);

alter table carts
    add constraint carts_users_id_fk
        foreign key (customer_id) references users (id);

