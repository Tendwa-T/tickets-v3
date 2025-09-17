
alter table tickets
    drop foreign key tickets_events_id_fk;

rename table tickets to ticket_types;

alter table ticket_types
    add name varchar(100) not null after event_id;

alter table ticket_types
    add description varchar(255) null after name;

alter table ticket_types
    add constraint ticket_types_events_id_fk
        foreign key (event_id) references events (id);

