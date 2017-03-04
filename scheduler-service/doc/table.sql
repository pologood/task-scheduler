CREATE TABLE qrtz_blob_triggers
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  TRIGGER_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  BLOB_DATA BLOB,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  CONSTRAINT qrtz_blob_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE INDEX SCHED_NAME ON qrtz_blob_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE TABLE qrtz_calendars
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  CALENDAR_NAME VARCHAR(100) NOT NULL,
  CALENDAR BLOB NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
);
CREATE TABLE qrtz_cron_triggers
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  TRIGGER_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  CRON_EXPRESSION VARCHAR(100) NOT NULL,
  TIME_ZONE_ID VARCHAR(80),
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  CONSTRAINT qrtz_cron_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_fired_triggers
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  ENTRY_ID VARCHAR(95) NOT NULL,
  TRIGGER_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  INSTANCE_NAME VARCHAR(100) NOT NULL,
  FIRED_TIME BIGINT(13) NOT NULL,
  SCHED_TIME BIGINT(13) NOT NULL,
  PRIORITY INT(11) NOT NULL,
  STATE VARCHAR(16) NOT NULL,
  JOB_NAME VARCHAR(100),
  JOB_GROUP VARCHAR(100),
  IS_NONCONCURRENT VARCHAR(1),
  REQUESTS_RECOVERY VARCHAR(1),
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, ENTRY_ID)
);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON qrtz_fired_triggers (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_JG ON qrtz_fired_triggers (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_J_G ON qrtz_fired_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON qrtz_fired_triggers (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON qrtz_fired_triggers (SCHED_NAME, INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_T_G ON qrtz_fired_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE TABLE qrtz_job_details
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  JOB_NAME VARCHAR(100) NOT NULL,
  JOB_GROUP VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(100),
  JOB_CLASS_NAME VARCHAR(100) NOT NULL,
  IS_DURABLE VARCHAR(1) NOT NULL,
  IS_NONCONCURRENT VARCHAR(1) NOT NULL,
  IS_UPDATE_DATA VARCHAR(1) NOT NULL,
  REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
  JOB_DATA BLOB,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE INDEX IDX_QRTZ_J_GRP ON qrtz_job_details (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON qrtz_job_details (SCHED_NAME, REQUESTS_RECOVERY);
CREATE TABLE qrtz_locks
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  LOCK_NAME VARCHAR(40) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, LOCK_NAME)
);
CREATE TABLE qrtz_paused_trigger_grps
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_scheduler_state
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  INSTANCE_NAME VARCHAR(100) NOT NULL,
  LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
  CHECKIN_INTERVAL BIGINT(13) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
);
CREATE TABLE qrtz_simple_triggers
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  TRIGGER_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  REPEAT_COUNT BIGINT(7) NOT NULL,
  REPEAT_INTERVAL BIGINT(12) NOT NULL,
  TIMES_TRIGGERED BIGINT(10) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  CONSTRAINT qrtz_simple_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_simprop_triggers
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  TRIGGER_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  STR_PROP_1 VARCHAR(512),
  STR_PROP_2 VARCHAR(512),
  STR_PROP_3 VARCHAR(512),
  INT_PROP_1 INT(11),
  INT_PROP_2 INT(11),
  LONG_PROP_1 BIGINT(20),
  LONG_PROP_2 BIGINT(20),
  DEC_PROP_1 DECIMAL(13,4),
  DEC_PROP_2 DECIMAL(13,4),
  BOOL_PROP_1 VARCHAR(1),
  BOOL_PROP_2 VARCHAR(1),
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  CONSTRAINT qrtz_simprop_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);
CREATE TABLE qrtz_task
(
  id INT(11) PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  task_group VARCHAR(100) DEFAULT '' NOT NULL COMMENT '任务所属项目',
  bean_name VARCHAR(100) DEFAULT '' NOT NULL COMMENT 'task 实例定时任务bean名称',
  job_name VARCHAR(100) DEFAULT '' NOT NULL COMMENT '任务名称',
  cron_expression VARCHAR(100) DEFAULT '' NOT NULL COMMENT '任务cron表达式',
  begin_time DATETIME COMMENT '任务开始执行时间',
  status TINYINT(4) DEFAULT '0' NOT NULL COMMENT '任务执行状态',
  end_time DATETIME COMMENT '任务结束执行时间',
  send_time DATETIME COMMENT '发送给task实例通知的时间',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '任务结束执行时间',
  retry_count INT(11) DEFAULT '0' NOT NULL COMMENT '重试次数',
  retry_counted INT(11) DEFAULT '0' NOT NULL COMMENT '已经重试的次数',
  path VARCHAR(32),
  fail TINYINT(1) DEFAULT '0',
  fail_reason TEXT
);
CREATE TABLE qrtz_task_record
(
  id INT(11) PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  task_group VARCHAR(100) DEFAULT '' NOT NULL COMMENT '任务所属项目',
  job_name VARCHAR(100) DEFAULT '' NOT NULL COMMENT '任务名称',
  task_id INT(11) DEFAULT '0' NOT NULL COMMENT 'task表主键',
  status TINYINT(4) DEFAULT '0' NOT NULL COMMENT '任务执行状态',
  begin_time DATETIME COMMENT '任务开始执行时间',
  end_time DATETIME COMMENT '任务结束执行时间',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
  fail TINYINT(1) DEFAULT '0',
  fail_reason TEXT
);
CREATE TABLE qrtz_triggers
(
  SCHED_NAME VARCHAR(100) NOT NULL,
  TRIGGER_NAME VARCHAR(100) NOT NULL,
  TRIGGER_GROUP VARCHAR(100) NOT NULL,
  JOB_NAME VARCHAR(100) NOT NULL,
  JOB_GROUP VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(100),
  NEXT_FIRE_TIME BIGINT(13),
  PREV_FIRE_TIME BIGINT(13),
  PRIORITY INT(11),
  TRIGGER_STATE VARCHAR(16) NOT NULL,
  TRIGGER_TYPE VARCHAR(8) NOT NULL,
  START_TIME BIGINT(13) NOT NULL,
  END_TIME BIGINT(13),
  CALENDAR_NAME VARCHAR(100),
  MISFIRE_INSTR SMALLINT(2),
  JOB_DATA BLOB,
  CONSTRAINT `PRIMARY` PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  CONSTRAINT qrtz_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP) REFERENCES qrtz_job_details (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
CREATE INDEX IDX_QRTZ_T_C ON qrtz_triggers (SCHED_NAME, CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON qrtz_triggers (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_J ON qrtz_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON qrtz_triggers (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON qrtz_triggers (SCHED_NAME, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON qrtz_triggers (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON qrtz_triggers (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON qrtz_triggers (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_STATE ON qrtz_triggers (SCHED_NAME, TRIGGER_STATE);
CREATE TABLE qrtz_task_email
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  task_group VARCHAR(16),
  job_name VARCHAR(16),
  task_id INT(11) DEFAULT '0' NOT NULL,
  email_address VARCHAR(32) NOT NULL
);