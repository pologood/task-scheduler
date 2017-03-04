package com.games.job.server.job;

import java.util.List;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.server.async.AsyncServiceImpl;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskEmail;
import com.games.job.server.repository.TaskEmailRepository;
import com.games.job.server.repository.TaskRepository;
import com.games.job.common.utils.BeanUtils;
import com.games.job.server.sender.EmailSenderImpl;
import com.games.job.server.service.TaskManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 任务失败处理
 */
@Component
public class TaskFeedJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(TaskFeedJob.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private EmailSenderImpl emailSender;

    @Autowired
    private TaskEmailRepository taskEmailRepository;


    private  static  final  Long  TIME_OUT_RANGE = 1000 * 60 * 10L;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("TaskStatusMonitorJob start");
        dealHaveSendStatusAndTimeOutTask();
        dealNoFeedBackStatusTask();
        logger.info("TaskStatusMonitorJob end");
    }

    private void dealHaveSendStatusAndTimeOutTask() {
        List<Task> list = taskRepository.findByStatus(TaskStatus.SEND.getId());
        if (CollectionUtils.isEmpty(list)) {
            logger.info("@execute  - no task have send");
            return;
        }
        list.stream().forEach(task -> {
            if (isTimeOutNoFeedBack(task)) {
                logger.info("@execute - set task status  noFeedBack  - para:{}", task);
                task.setStatus(TaskStatus.NOFEEDBACK.getId());
                taskRepository.save(task);
            }
        });
    }

    public boolean isTimeOutNoFeedBack(Task task) {
        if (System.currentTimeMillis() - task.getSendTime().getTime() > TIME_OUT_RANGE) {
            return true;
        }
        return false;
    }

    private void dealNoFeedBackStatusTask() {
        List<Task> list = taskRepository.findByStatus(TaskStatus.NOFEEDBACK.getId());
        if (CollectionUtils.isEmpty(list)) {
            logger.info("@execute  - no task  need run");
            return;
        }
        list.stream().forEach(task -> {
            if (task.getRetryCount() > task.getRetryCounted()) {
                logger.info("@execute - retry notify task instance - para:{}", task);
                TaskModel taskModel = new TaskModel();
                BeanUtils.copyProperties(task, taskModel);
                taskRepository.incRetryCountById(task.getId());
                taskManager.sendTask(taskModel);
            } else {
                logger.info("@execute - over retry count set retryFail status - para:{}", task);
                task.setStatus(TaskStatus.RETRYFAIL.getId());
                taskRepository.save(task);
                List<TaskEmail> emails = this.taskEmailRepository.findByTaskId(task.getId());
                String title=task.getTaskGroup()+"-"+task.getJobName()+"执行异常"+task.getSendTime();
                String content=task.getFailReason();
                emailSender.sendTaskFail(emails,title,content);
            }
        });
    }
}