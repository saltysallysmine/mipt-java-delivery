package com.mipt.delivery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class MainController {

    @PostMapping("/{city}")
    void RedirectDelivery(@RequestParam String city) {
        log.info("Got new message from " + city);
    }

}
