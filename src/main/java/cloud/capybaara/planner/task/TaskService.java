package cloud.capybaara.planner.task;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.appuser.AppUser;
import cloud.capybaara.planner.appuser.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserRepository userRepository;

    public Task create(TaskRequest taskRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        var task = Task.builder()
                .description(taskRequest.getDescription())
                .taskPriority(taskRequest.getTaskPriority())
                .completed(taskRequest.getCompleted())
                .userId(user.getId())
                .build();

        taskRepository.save(task);

        return task;
    }

    public List<Task> getUserTasks(String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        return taskRepository.findAllByUserId(user.getId()).stream().toList();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task edit(TaskEditRequest taskEditRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        // TODO: написать логику изменения теска!
        var taskInRepo = taskRepository.findTaskById(taskEditRequest.getId()).orElseThrow();
        //if ()
        //if (Objects.equals(taskInRepo.get().getUserId(), user.getId()))



        return taskInRepo;
    }

    public Boolean delete(TaskDeleteRequest taskDeleteRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        Long taskId = taskDeleteRequest.getId();
        var taskInRepo = taskRepository.findTaskById(taskId);

        if (taskInRepo.isPresent()) {
            taskRepository.deleteById(taskId);
        }

        System.out.println("Task with id " + taskId + " has been deleted by user ID#" + user.getId());

        return true;
    }
}
