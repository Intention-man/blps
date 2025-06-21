package aviasales.ytsaurus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ytsaurus")
public class YTsaurusController {
    private final YTsaurusService service;

    @PostMapping("/add")
    public void addStatistics() {
        service.addStatistics();
    }
}
