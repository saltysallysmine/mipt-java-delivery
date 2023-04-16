package com.mipt.delivery.controllers;

import com.google.gson.Gson;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Data
    private static class OrderRequest {
        String city;
    }

    private String getJsonOfOrderRequest(String cityName) {
        Gson gson = new Gson();
        OrderRequest request = new OrderRequest();
        request.setCity(cityName);
        return gson.toJson(request, OrderRequest.class);
    }

    @Test
    void RedirectDeliveryTest() throws Exception {
        // Send request from Moscow
        String content = getJsonOfOrderRequest("Moscow");
        mockMvc.perform(post("/delivery/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
        Thread.sleep(1000);
        // Send request from Saint-Petersburg
        content = getJsonOfOrderRequest("Saint_Petersburg");
        mockMvc.perform(post("/delivery/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
        Thread.sleep(1000);
        // Send request from unknown city
        content = getJsonOfOrderRequest("Unknown-City");
        mockMvc.perform(post("/delivery/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

}