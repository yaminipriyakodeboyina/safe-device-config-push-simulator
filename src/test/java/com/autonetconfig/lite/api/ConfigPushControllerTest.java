package com.autonetconfig.lite.api;

import com.autonetconfig.lite.AutoNetConfigLiteApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AutoNetConfigLiteApplication.class)
@AutoConfigureMockMvc
class ConfigPushControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsDeviceLockedWhenSameDeviceHasRunningJob() throws Exception {
        mockMvc.perform(post("/config-push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson("device-sea-001", "set interfaces xe-0/0/0 description google-cloud-edge", "interface")))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.jobId", notNullValue()));

        mockMvc.perform(post("/config-push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson("device-sea-001", "set routing-options static route 10.0.1.0/24 next-hop 192.168.1.9", "routing")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", equalTo("DEVICE_LOCKED")));
    }

    private String requestJson(String deviceId, String configChange, String changeType) {
        return "{\n" +
                "  \"deviceId\": \"" + deviceId + "\",\n" +
                "  \"configChange\": \"" + configChange + "\",\n" +
                "  \"changeType\": \"" + changeType + "\"\n" +
                "}";
    }
}
