create table password_resets
(
    id         BIGINT auto_increment
        primary key,
    user_id    BIGINT                                  not null,
    token      varchar(255)                            not null,
    expires_at timestamp                               not null,
    used       BOOLEAN   default false                 not null,
    created_at TIMESTAMP default (CURRENT_TIMESTAMP()) not null,
    constraint password_resets_uk_token
        unique (token),
    constraint password_resets___fk
        foreign key (user_id) references users (id)
            on delete cascade
);

