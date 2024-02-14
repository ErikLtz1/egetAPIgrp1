
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
        postUserUUID uuid,
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
