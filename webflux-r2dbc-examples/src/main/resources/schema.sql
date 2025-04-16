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
    id            char(19)         not null primary key,
    name          varchar(10)      not null,
    number        varchar(15)      not null,
    password      varchar(65)      not null,-- 密码签名进行校验65位
    description   varchar(200)     null,
    department_id char(19)         not null,
    role          char(4)          not null,-- 存储角色，随机权值
    group_number  tinyint unsigned null,-- unsigned无符号，即大于等于
    student       json             null comment '{"teacherId", "teacherName", "queueNumber", "projectTitle"}',
    teacher       json             null comment '{"A", "C", "total"}',
    insert_time   datetime         not null default current_timestamp,
    update_time   datetime         not null default current_timestamp on update current_timestamp,

    unique (number),
    index ((cast(student ->> '$.teacherId' as char(19)) collate utf8mb4_bin)),
    index (department_id, role, group_number)
-- 账号密码在业务层面比较
);

/*
三个过程，但每个过程的子项不相同个数不定，则items采用数组，子项分数写入数组对象
*/
create table if not exists `process`
(
    id             char(19)         not null primary key,
    name           varchar(20)      not null,
    items          json             null comment '[{"number", "name", "point", "description"}]',
    point          tinyint unsigned null,
    auth           char(5)          not null,
    department_id  char(19)         not null,
    student_attach json             null comment '[{"number", "name", "ext", "description"}]',
    insert_time    datetime         not null default current_timestamp,
    update_time    datetime         not null default current_timestamp on update current_timestamp,
    index (department_id)
);


/*
每名老师为每位学生在某过程下评分唯一
老师针对哪一个过程下的学生进行打分
老师会查自己所带组的情况，索引process_id，teacher_id，group
复合索引 index (student_id,process_id,teacher_id),第一个命中之后才会往后查，所以要根据需求定第一个
*/
create table if not exists `process_score`
(
    id          char(19) not null primary key,
    student_id  char(19) not null,
    process_id  char(19) not null,
    teacher_id  char(19) not null,
    detail      json     not null comment '{"teacherName", "score", detail: [{"number", "score"}]}',
    insert_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp,

    unique (process_id, student_id, teacher_id)
);


create table if not exists `process_file`
(
    id          char(19)         not null primary key,
    detail      varchar(60)      null,
    student_id  char(19)         not null,
    process_id  char(19)         not null,
    number      tinyint unsigned not null,
    insert_time datetime         not null default current_timestamp,
    update_time datetime         not null default current_timestamp on update current_timestamp,

    unique (process_id, student_id, number)
);