package com.games.job.server.repository;


import com.games.job.server.entity.TaskEmail;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TaskEmailRepository extends JpaRepository<TaskEmail,Integer> {

    public List<TaskEmail> findByTaskId(Integer taskId);
}
