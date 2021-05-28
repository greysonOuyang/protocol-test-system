-- auto-generated definition
create table param
(
    param_id           int auto_increment comment '����'
        primary key,
    param_interface_id varchar(32)  null comment '����id,��interfaceConfig��id��Ӧ',
    param_key_id       varchar(32)  null comment '��ʶid',
    param_index        int          null comment 'д���±�',
    param_length       varchar(10)  null comment '��������',
    param_field        varchar(50)  null comment '��������',
    param_value        varchar(500) null comment '����ֵ',
    param_type         varchar(10)  null comment '��������',
    param_io           varchar(10)  null comment '�������',
    extend3            int          null,
    extend4            int          null,
    extend5            int          null,
    extend6            int          null,
    extend7            int          null
)
    comment '������';

-- auto-generated definition
create table t_config
(
    config_key          varchar(32) null comment '����Key',
    config_name         varchar(32) null comment '��������',
    config_type         varchar(32) null comment '��������',
    config_value        varchar(32) null comment '���Ͷ�Ӧֵ',
    config_id           int auto_increment comment 'Ψһ��ʶ��'
        primary key,
    Interface_config_id varchar(32) null comment '�ӿ�����id',
    config_key_id       varchar(32) null comment 'Ψһ��ʶ��'
)
    comment '���ñ�';

-- auto-generated definition
create table t_interface_config
(
    interface_config_id varchar(32)  null comment 'Ψһ��ʶ��',
    request_name        varchar(32)  null comment '��������',
    request_type        varchar(32)  null comment '��������',
    url                 varchar(32)  null comment '�����ַ',
    request_method      varchar(32)  null comment '����ʽ',
    content             varchar(500) null comment '��������',
    port                int          null comment '�˿ں�',
    new_column          int          null,
    key_id              int auto_increment
        primary key,
    currentMode         varchar(12)  null comment '�ͻ���/�����'
)
    comment '�ӿ����ñ�';