package com.games.job.server.repository;


import com.games.job.server.entity.TaskRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TaskRecordRepository  extends JpaRepository<TaskRecord,Integer> {

     public List<TaskRecord> findByTaskId(Integer taskId);
}