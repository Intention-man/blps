package aviasales.scheduled;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduled")
@AllArgsConstructor
public class SchedulerController {
    private final SchedulerService scheduler;

    @PostMapping("")
    public void activeJob(@RequestParam String classname, @RequestParam boolean setactive) throws Exception {
        scheduler.enableOrDisableJob(classname, setactive);
    }
}