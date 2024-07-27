package cloud.capybaara.planner.task;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public List<Task> getUserTasks(Principal principal) {
        return taskService.getUserTasks(principal.getName());
    }

    @PostMapping(path = "/create")
    public Task create(@RequestBody TaskRequest taskRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        return taskService.create(taskRequest, userEmail);
    }

    @PostMapping(path = "/edit")
    public boolean edit(@RequestBody TaskEditRequest taskEditRequest, Principal principal) {
        String userEmail = principal.getName();

        return taskService.edit(taskEditRequest, userEmail);
    }

    @PostMapping(path = "/delete")
    public boolean delete(@RequestBody TaskDeleteRequest taskDeleteRequest, Principal principal) {
        String userEmail = principal.getName();

        return taskService.delete(taskDeleteRequest, userEmail);
    }

    //TODO: сделать доступ только для админа
    /*@GetMapping(path = "/all")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }*/
}
