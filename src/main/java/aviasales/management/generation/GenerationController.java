package aviasales.management.generation;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tickets/editor-api")
@AllArgsConstructor
public class GenerationController {
    private final GenerationService generationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateTickets(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        generationService.generateAndSaveTickets(date);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
