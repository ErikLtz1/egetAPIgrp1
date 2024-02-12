
    create sequence t_developer_SEQ start with 1 increment by 50;

    create sequence t_post_SEQ start with 1 increment by 50;

    create sequence t_user_SEQ start with 1 increment by 50;

    create table t_developer (
        id bigint not null,
        apiKey uuid,
        email varchar(255),
        password varchar(255),
        salt varchar(255),
        primary key (id)
    );

    create table t_post (
        price float(53) not null,
        id bigint not null,
        postUserUUid uuid,
        imgUrl varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table t_user (
        id bigint not null,
        userUUID uuid,
        password varchar(255),
        username varchar(255),
        primary key (id)
    );
insert into t_developer (apiKey, email, password, id) values ('3b6d0cc9-aa9e-4b98-b3c6-237df1b25b63', 'kcadle0@gov.uk', 'Diyarbakir Airport', 1);
insert into t_developer (apiKey, email, password, id) values ('c3a1e575-fd77-481c-8c24-79a0573a9846', 'sdebold1@uol.com.br', 'Laverton Airport', 2);
insert into t_developer (apiKey, email, password, id) values ('0fa931c1-083f-4c85-87dc-2a0769e15383', 'oparcells2@tamu.edu', 'Selbang Airport', 3);
insert into t_user (userUUID, username, password, id) values ('627d776e-7637-46c2-8ee5-666b66c7b36a', 'abc', '123', 1);
insert into t_user (userUUID, username, password, id) values ('bb5b1adc-2eac-441b-a873-d08bdda9cbbe', 'def', '456', 2);
insert into t_user (userUUID, username, password, id) values ('0dbeb02b-34c2-4e27-9794-b93996ac801e', 'ghi', '789', 3);
