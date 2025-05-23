package com.example.webfluxr2dbcexamples.service;

import com.example.webfluxr2dbcexamples.dox.User;
import com.example.webfluxr2dbcexamples.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Transactional
    @EventListener(classes = ApplicationReadyEvent.class)
    public Mono<Void> onApplicationReadyEvent() {
        String number = "admin";
        return userRepository.count()
                .flatMap(r -> {
                    if (r == 0) {
                        User admin = User.builder()
                                .name(number)
                                .number(number)
                                .password(encoder.encode(number))
                                .role(User.ROLE_ADMIN)
                                .departmentId(number)
                                .build();
                        return userRepository.save(admin).then();
                    }
                    return Mono.empty();
                });
    }
}