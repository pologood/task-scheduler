package com.games.job.server.service.channel;

import com.games.job.common.enums.HttpMethod;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.games.job.common.utils.NetUtils;
import com.games.job.server.async.AsyncService;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskEmail;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.enums.ResponseCode;
import com.games.job.server.repository.TaskEmailRepository;
import com.games.job.server.repository.TaskRepository;
import com.games.job.server.sender.EmailSender;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:08
 * @project:job-center
 * @full_name:com.games.job.server.service.channel.RestfulChannel
 * @ide:IntelliJ IDEA
 */
public class RestfulChannel implements Channel{

    private static final Logger logger = LoggerFactory.getLogger(RestfulChannel.class);

    private TaskRepository taskRepository;

    private EmailSender emailSender;

    private TaskEmailRepository taskEmailRepository;


    @Override
    public Set<TaskModel> getTask() {
        return null;
    }

    @Override
    public Set<TaskModel> getTaskStatus() {
        return null;
    }

    @Override
    public void putTask(TaskModel taskModel) {
        fireRestfulTask(taskModel);
    }


    private void fireRestfulTask(TaskModel taskModel){
        Task task = taskRepository.findById(taskModel.getTaskId());
        if(task!=null&&StringUtils.isNotBlank(task.getPath())){
            if(task.getRetryCount() > task.getRetryCounted()){
                try {
                    Map<String,String> params = Maps.newConcurrentMap();
                    params.put("id",task.getId()+"");
                    String result = NetUtils.sendMap(task.getPath(),params, HttpMethod.GET);
                    if(StringUtils.isNotBlank(result)){
                        Result ret = JsonUtils.fromJson(result,Result.class);
                        if(ret.getCode()!= ResponseCode.OPT_OK.getCode()){
                            taskRepository.incRetryCountById(task.getId());
                            fireRestfulTask(taskModel);
                        }
                    }else{
                        taskRepository.incRetryCountById(task.getId());
                        fireRestfulTask(taskModel);
                    }
                }catch (Exception e){
                    taskRepository.incRetryCountById(task.getId());
                    fireRestfulTask(taskModel);
                }
            }else{
                logger.info("@execute restful - over retry count set retryFail status - para:{}", task);
                task.setStatus(TaskStatus.RETRYFAIL.getId());
                taskRepository.save(task);
                List<TaskEmail> emails = taskEmailRepository.findByTaskId(task.getId());
                String title=task.getTaskGroup()+"-"+task.getJobName()+"执行异常";
                String content=task.getFailReason();
                emailSender.sendTaskFail(emails,title,content);
            }
        }
    }

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void setSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void setTaskEmailRepository(TaskEmailRepository taskEmailRepository) {
        this.taskEmailRepository = taskEmailRepository;
    }
}
