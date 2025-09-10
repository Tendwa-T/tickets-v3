create table users
(
    id            BIGINT auto_increment comment 'The User''s unique Identifying Number'
        primary key,
    email         varchar(255)                           not null comment 'User Email',
    password_hash varchar(255)                           not null comment 'Encrypted Password',
    first_name    varchar(255)                           not null comment 'User''s first Name',
    last_name     varchar(255)                           not null comment 'User''s last name',
    phone_number  varchar(50)                            not null comment 'User''s phone Number',
    is_active     BOOLEAN  default true                  not null,
    is_deleted    BOOLEAN  default false                 not null,
    created_at    Datetime default (current_timestamp()) not null,
    updated_at    DATETIME default (current_timestamp()) not null,
    constraint user_uk_email
        unique (email)
);

create table roles
(
    id   BIGINT auto_increment
        primary key,
    name ENUM ('ATTENDEE', 'ORGANIZER', 'ADMIN') default 'ATTENDEE' not null,
    constraint roles_uk_name
        unique (name)
);

create table user_roles
(
    id         BIGINT auto_increment
        primary key,
    user_id    BIGINT                                  not null,
    role_id    BIGINT                                  not null,
    created_at TIMESTAMP default (CURRENT_TIMESTAMP()) not null,
    constraint user_roles_roles_id_fk
        foreign key (role_id) references roles (id),
    constraint user_roles_users_id_fk
        foreign key (user_id) references users (id)
);


