alter table events
    add constraint events_users_id_fk_2
        foreign key (created_by) references users (id);

