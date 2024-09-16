create table if not exists `department`
(
    id          char(19)    not null primary key,
    name        varchar(20) not null,
    insert_time datetime    not null default current_timestamp,
    update_time datetime    not null default current_timestamp on update current_timestamp
);
/*
COMMENT子句用于给表或列添加注释
cast(department ->> '$.depId' as char(19)): 使用JSON路径表达式->>来提取JSON对象中depId的值，并将其转换为长度为19的字符类型
collate utf8mb4_bin: 指定排序规则为二进制比较方式，这意味着在比较时会严格区分大小写和空格
*/
create table if not exists `user`
(
    id          char(19) not null primary key,
    name        varchar(45) not null, -- 昵称
    account     varchar(15) not null , -- 账号
    password    varchar(20) not null,
    role        char(4) not null , -- 存储角色
    department  json comment '{"depId", "name"}',
    create_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp,
    index ((cast(department ->> '$.depId' as char(19)) collate utf8mb4_bin)),
    index (role)
);


/*
三个过程，但每个过程的子项不相同个数不定，则items采用数组，子项分数写入数组对象
*/
create table if not exists `process`
(
    id          char(19)    not null primary key,
    name        varchar(20) not null,
    items       json null comment '[{"number", "name", "point", "description"}]',
    insert_time datetime    not null default current_timestamp,
    update_time datetime    not null default current_timestamp on update current_timestamp
);

/*
每名老师为每位学生在某过程下评分唯一
*/
create table if not exists `process_score`
(
    id          char(19) not null primary key,
    student_id  char(19) not null,
    process_id  char(19) not null,
    teacher_id  char(19) not null,
    detail      json     not null comment '{"teacherName","detail":[{"number","score"}]}',
    insert_time datetime    not null default current_timestamp,
    update_time datetime    not null default current_timestamp on update current_timestamp,
    unique (student_id,process_id,teacher_id)
)