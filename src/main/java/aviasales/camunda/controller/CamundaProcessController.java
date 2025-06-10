package aviasales.camunda.controller;

import aviasales.security.data.AuthenticationRequest;
import aviasales.security.data.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/camunda-process")
@RequiredArgsConstructor
public class CamundaProcessController {

    private final RuntimeService runtimeService;

    @PostMapping("/register")
    @Operation(summary = "Start Camunda Registration Process")
    public ResponseEntity<Map<String, String>> startRegistrationProcess(@RequestBody RegisterRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", request.getUsername());
        variables.put("password", request.getPassword());
        variables.put("adminCode", request.getAdminCode());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth_process", variables);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration process started.");
        response.put("processInstanceId", processInstance.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Start Camunda Authentication Process")
    public ResponseEntity<Map<String, String>> startAuthenticationProcess(@RequestBody AuthenticationRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", request.getUsername());
        variables.put("password", request.getPassword());
        variables.put("action", "login");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth_process", variables);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Authentication process started.");
        response.put("processInstanceId", processInstance.getId());
        return ResponseEntity.ok(response);
    }
}
