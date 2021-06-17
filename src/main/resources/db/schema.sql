create table param
(
    param_id           int auto_increment comment '主键'
        primary key,
    param_interface_id varchar(32)  null comment '关联id,与interfaceConfig表id对应',
    param_key_id       varchar(32)  null comment '标识id',
    param_index        int          null comment '写入下标',
    param_length       varchar(10)  null comment '参数长度',
    param_field        varchar(50)  null comment '参数名称',
    param_value        varchar(500) null comment '参数值',
    param_type         varchar(10)  null comment '参数类型',
    param_io           varchar(10)  null comment '输入输出',
    extend3            int          null,
    extend4            int          null,
    extend5            int          null,
    extend6            int          null,
    extend7            int          null
)
    comment '参数表';

create table t_config
(
    config_key          varchar(32) null comment '配置Key',
    config_name         varchar(32) null comment '配置名称',
    config_type         varchar(32) null comment '配置类型',
    config_value        varchar(32) null comment '类型对应值',
    config_id           int auto_increment comment '唯一标识符'
        primary key,
    Interface_config_id varchar(32) null comment '接口配置id',
    config_key_id       varchar(32) null comment '唯一标识符'
)
    comment '配置表';

create table t_interface_config
(
    interface_config_id varchar(32)  null comment '唯一标识符',
    request_name        varchar(32)  null comment '请求名称',
    request_type        varchar(32)  null comment '请求类型',
    url                 varchar(32)  null comment '请求地址',
    request_method      varchar(32)  null comment '请求方式',
    content             varchar(500) null comment '请求内容',
    port                int          null comment '端口号',
    description         varchar(32)  null,
    key_id              int auto_increment
        primary key,
    currentMode         varchar(12)  null comment '客户端/服务端'
)
    comment '接口配置表';

create table t_project
(
    project_id          bigint auto_increment comment '主键Id'
        primary key,
    project_name        varchar(50)  null comment '项目名称',
    encoder_id          int          not null comment '编码器',
    decoder_id          int          not null comment '解码器',
    message_description varchar(100) null comment '消息类型描述',
    message_type        varchar(30)  null comment '消息类型'
)
    comment '服务端接口表';

