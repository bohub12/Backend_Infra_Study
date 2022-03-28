drop table if exists image;

create table image
(
    fileId   bigint       not null,
    fileName varchar(255) not null,
    viewName varchar(255) not null,
    filePath varchar(255) not null,
    suffix   varchar(255) not null,
    primary key (fileId)
);