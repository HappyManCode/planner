package cloud.capybaara.planner.event;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.appuser.AppUser;
import cloud.capybaara.planner.appuser.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AppUserRepository userRepository;

    public Event create(EventRequest eventRequest, String userEmail) {
        AppUser user = userRepository.findAppUserByEmail(userEmail).orElseThrow();

        var event = Event.builder()
                .date(eventRequest.getDate())
                .description(eventRequest.getDescription())
                .completed(eventRequest.getCompleted())
                .userId(user.getId())
                .build();

        eventRepository.save(event);

        return event;
    }

    public List<Event> getUserEvents(String userName) {
        AppUser user = userRepository.findAppUserByEmail(userName).orElseThrow();

        return eventRepository.findAllByUserId(user.getId()).stream().toList();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
