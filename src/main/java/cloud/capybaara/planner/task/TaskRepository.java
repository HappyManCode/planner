package cloud.capybaara.planner.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findTaskById(Long Id);

    List<Task> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Task a SET a.description = ?2, a.taskPriority = ?3, a.completed = ?4 WHERE a.id = ?1")
    int updateTask(Long taskId, String description, TaskPriority taskPriority, Boolean completed);
}
