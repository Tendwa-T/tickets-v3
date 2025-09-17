alter table events
    modify status enum ('DRAFT', 'PUBLISHED', 'CANCELLED', 'COMPLETED', 'EXPIRED') default 'DRAFT' not null;

