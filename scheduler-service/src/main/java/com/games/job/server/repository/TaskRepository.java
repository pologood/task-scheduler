package com.games.job.server.repository;

import java.util.List;

import com.games.job.server.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    Task  findByTaskGroupAndJobName(String TaskGroup,String jobName);

    List<Task>  findByStatus(Integer status);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value="update qrtz_task set retry_counted = retry_counted+1  where id =?1")
    void  incRetryCountById(Integer id);

    @Query(nativeQuery = true, value="select * from qrtz_task where id =?1")
    Task findById(Integer id);

}
