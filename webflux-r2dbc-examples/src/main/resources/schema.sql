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
    name        varchar(6) not null, -- 昵称
    account     varchar(12) not null , -- 账号
    password    varchar(65) not null,-- 密码签名进行校验65位
    role        char(4) not null , -- 存储角色，随机权值
    `group`     tinyint unsigned null , -- unsigned无符号，即大于等于
    student     json not null comment '{q,projectTitle,teacherId,teacherName}',
    teahcer     json not null comment '{total,A,C}',
    department  json null comment '{depId, name}', -- 可空，admin
    create_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp,
    unique (account),
    index (((cast(department ->> '$.depId' as char(19)) collate utf8mb4_bin)),role), -- 根据某专业下角色，得到user
    index ((cast(student ->> '$.teacherId' as char(19)) collate utf8mb4_bin)) -- 给老师id会找到这些user

-- 账号密码在业务层面比较
);


/*
三个过程，但每个过程的子项不相同个数不定，则items采用数组，子项分数写入数组对象
*/
create table if not exists `process`
(
    id          char(19)    not null primary key,
    name        varchar(20) not null,
    `desc`      varchar(100) null ,
    department_id char(19) not null ,
    point       tinyint unsigned not null ,
    department  json not null comment '{depId, name}',
    items       json not null comment '[{number, name, point, description}]',
    # 针对谁?
    type        char(4) not null ,
    attach      json null comment '[{number,name,exp}]', -- 扩展名校验
    insert_time datetime    not null default current_timestamp,
    update_time datetime    not null default current_timestamp on update current_timestamp,
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
    scores      json not null comment '[teacherName,scores,detail:{number,score}]',
    insert_time datetime    not null default current_timestamp,
    update_time datetime    not null default current_timestamp on update current_timestamp,
    unique (student_id,process_id,teacher_id)
)