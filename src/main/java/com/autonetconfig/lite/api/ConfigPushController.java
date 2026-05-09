package com.autonetconfig.lite.api;

import com.autonetconfig.lite.dto.AuditRecordResponse;
import com.autonetconfig.lite.dto.ConfigPushRequest;
import com.autonetconfig.lite.dto.ConfigPushResponse;
import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.service.ConfigPushOrchestrator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class ConfigPushController {
    private final ConfigPushOrchestrator orchestrator;

    public ConfigPushController(ConfigPushOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/config-push")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ConfigPushResponse submit(@Valid @RequestBody ConfigPushRequest request) {
        return ConfigPushResponse.from(orchestrator.submit(request));
    }

    @GetMapping("/config-push/{jobId}")
    public ConfigPushResponse getStatus(@PathVariable String jobId) {
        ConfigPushJob job = orchestrator.findJob(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Config push job not found."));
        return ConfigPushResponse.from(job);
    }

    @GetMapping("/audit-log")
    public List<AuditRecordResponse> auditLog() {
        return orchestrator.auditLog().stream()
                .map(AuditRecordResponse::from)
                .collect(Collectors.toList());
    }
}
