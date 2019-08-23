package com.web.poc1.config;

import com.web.poc1.dao.UserRepository;
import com.web.poc1.model.User;
import com.web.poc1.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class StartupTransactionHandler {

    private UserRepository userRepository;
    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultUser() {
        if (userRepository.count() == 0) {
            User defaultAdminUser = new User();
            defaultAdminUser.setAdmin(true);
            this.userService.register(defaultAdminUser, "admin1", "admin1", true);
            log.info("Created default admin user");
        }
    }
}
