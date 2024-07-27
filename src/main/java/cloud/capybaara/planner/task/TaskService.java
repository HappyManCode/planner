package cloud.capybaara.planner.task;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.appuser.AppUser;
import cloud.capybaara.planner.appuser.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserRepository userRepository;

    public Task create(TaskRequest taskRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        var task = new Task(
                taskRequest.getDescription(),
                taskRequest.getTaskPriority(),
                taskRequest.getCompleted(),
                user.getId()
        );

        taskRepository.save(task);

        return task;
    }

    public List<Task> getUserTasks(String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        return taskRepository.findAllByUserId(user.getId()).stream().toList();
    }

    //TODO: админский метод
    /*public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }*/

    public boolean edit(TaskEditRequest taskEditRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        Long taskId = taskEditRequest.getId();
        Optional<Task> existingTask = taskRepository.findTaskById(taskEditRequest.getId());

        if (existingTask.isEmpty()) {
            System.out.println("Task with id " + taskId + " was not found at user ID#" + user.getId());
            return false;
        }

        Task updatedTask = new Task();
        updatedTask.setId(taskId);

        if (taskEditRequest.getDescription() != null) {
            updatedTask.setDescription(taskEditRequest.getDescription());
        } else {
            updatedTask.setDescription(existingTask.get().getDescription());
        }

        if (taskEditRequest.getTaskPriority() != null) {
            updatedTask.setTaskPriority(taskEditRequest.getTaskPriority());
        } else {
            updatedTask.setTaskPriority(existingTask.get().getTaskPriority());
        }

        if (taskEditRequest.getCompleted() != null) {
            updatedTask.setCompleted(taskEditRequest.getCompleted());
        } else {
            updatedTask.setCompleted(existingTask.get().getCompleted());
        }

        int resultCode = taskRepository.updateTask(
                taskId,
                updatedTask.getDescription(),
                updatedTask.getTaskPriority(),
                updatedTask.getCompleted()
        );
        if (resultCode == 1) {
            System.out.println("Task with id " + taskId + " has been updated:\n" + "description: "
                    + taskEditRequest.getDescription() + "\n" + "taskPriority: " + taskEditRequest.getTaskPriority()
                    + "\n" + "completed: " + taskEditRequest.getCompleted());
            return true;
        }

        System.out.println("An error occurred while updating the data in the database for Task #" + taskId);
        return false;
    }

    public boolean delete(TaskDeleteRequest taskDeleteRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        Long taskId = taskDeleteRequest.getId();
        var taskInRepo = taskRepository.findTaskById(taskId);

        if (taskInRepo.isPresent()) {
            taskRepository.deleteById(taskId);

            System.out.println("Task with id " + taskId + " has been deleted by user ID#" + user.getId());
            return true;
        }

        System.out.println("Task with id " + taskId + " was not found at user ID#" + user.getId());
        return false;
    }
}
