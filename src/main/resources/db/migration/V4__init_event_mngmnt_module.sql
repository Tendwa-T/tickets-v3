create table venues
(
    id         BIGINT auto_increment
        primary key,
    name       VARCHAR(255)                            not null,
    address    VARCHAR(500)                            null,
    city       VARCHAR(100)                            null,
    country    VARCHAR(100)                            null,
    capacity   INT                                     not null,
    is_deleted BOOLEAN   default false                 not null,
    created_by BIGINT                                  not null,
    created_at TIMESTAMP default (CURRENT_TIMESTAMP()) not null,
    constraint venues_users_id_fk
        foreign key (created_by) references users (id)
);

create table events
(
    id          BIGINT auto_increment
        primary key,
    organizer_id   BIGINT                                                                              not null,
    title       VARCHAR(255)                                                                        not null,
    description TEXT                                                                                not null,
    venue_id       BIGINT                                                                              not null,
    start_time  TIMESTAMP                                                                           not null,
    end_time    timestamp                                                                           not null,
    capacity    int                                                                                 not null,
    status      ENUM ('DRAFT', 'PUBLISHED', 'CANCELLED', 'COMPLETED') default 'DRAFT'               not null,
    created_by  BIGINT                                                                              not null,
    created_at  DATETIME                                              default (CURRENT_TIMESTAMP()) not null,
    updated_at  DATETIME                                              default (CURRENT_TIMESTAMP()) not null,
    constraint events_users_id_fk
        foreign key (organizer_id) references users (id),
    constraint events_venues_id_fk
        foreign key (venue_id) references venues (id)
);

create table tickets
(
    id             BIGINT auto_increment
        primary key,
    event_id       BIGINT                                 not null,
    type           VARCHAR(100)                           not null,
    price          DECIMAL(10, 2)                         not null,
    quantity_total INT                                    not null,
    quantity_sold  INT      default 0                     not null,
    created_by     BIGINT                                 not null,
    created_at     DATETIME default (CURRENT_TIMESTAMP()) not null,
    constraint tickets_events_id_fk
        foreign key (event_id) references events (id)
);

