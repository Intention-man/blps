package aviasales.ytsaurus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ytsaurus")
public class YTsaurusController {
    private final YTsaurusService service;

    @PostMapping("/add")
    public void addStatistics() throws Exception {
        service.addStatistics();
    }
}
