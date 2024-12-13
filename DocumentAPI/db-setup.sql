create table directory
(
    directory_id        varchar(32)  not null
        primary key,
    display_name        varchar(255) not null,
    parent_directory_id varchar(32)  not null,
    owner_id            int          not null comment 'owner_id',
    constraint directory_uq_parent_name
        unique (parent_directory_id, display_name),
    constraint directory_directory_directory_id_fk
        foreign key (parent_directory_id) references directory (directory_id)
            on delete cascade
);

create table document
(
    document_id         varchar(32)                        not null
        primary key,
    display_name        varchar(255)                       not null,
    content             mediumtext                         not null,
    parent_directory_id varchar(32)                        not null,
    owner_id            int                                not null,
    date_created        datetime default CURRENT_TIMESTAMP not null,
    date_changed        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint document_uq_directory_name
        unique (parent_directory_id, display_name),
    constraint document_directory_directory_id_fk
        foreign key (parent_directory_id) references directory (directory_id)
            on delete cascade
);