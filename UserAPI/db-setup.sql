create table user
(
    user_id           int auto_increment
        primary key,
    display_name      varchar(255) not null,
    email             varchar(320) not null,
    password          varchar(72)  not null,
    root_directory_id varchar(32)  not null,
    constraint user_uq_email
        unique (email)
);