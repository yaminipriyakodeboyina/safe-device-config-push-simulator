package com.autonetconfig.lite.api;

import com.autonetconfig.lite.AutoNetConfigLiteApplication;
import com.autonetconfig.lite.service.DeviceLockManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AutoNetConfigLiteApplication.class)
@AutoConfigureMockMvc
class ConfigPushControllerTest {
    private static final String TEST_DEVICE_ID = "device-sea-001";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceLockManager lockManager;

    @AfterEach
    void unlockTestDevice() {
        lockManager.unlock(TEST_DEVICE_ID);
    }

    @Test
    void returnsDeviceLockedWhenSameDeviceHasRunningJob() throws Exception {
        lockManager.tryLock(TEST_DEVICE_ID);

        mockMvc.perform(post("/config-push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(TEST_DEVICE_ID, "set routing-options static route 10.0.1.0/24 next-hop 192.168.1.9", "routing")))
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
