package org.quizzer.category.api;

import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("it")
public class ApplicationController {
    @GetMapping("status")
    public void status() { }

    @PostMapping("restart")
    public void restart() {
        Restarter.getInstance().restart();
    }
}
