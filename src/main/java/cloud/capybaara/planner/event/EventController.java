package cloud.capybaara.planner.event;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/event")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping()
    public List<Event> getUserEvents(Principal principal) {
        return eventService.getUserEvents(principal.getName());
    }

    @PostMapping(path = "/create")
    public Event create(@RequestBody EventRequest eventRequest, Principal principal) {
        String userEmail = principal.getName();
        return eventService.create(eventRequest, userEmail);
    }

    //TODO: сделать доступ только для админа
    @GetMapping(path = "/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
}
