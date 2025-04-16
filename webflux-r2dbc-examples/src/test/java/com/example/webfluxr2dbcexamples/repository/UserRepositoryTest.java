package com.example.webfluxr2dbcexamples.repository;

import com.example.webfluxr2dbcexamples.dox.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByRoleAndGroupNumber() {
        // 构建用于测试的 User 对象
        User user = User.builder()
                .role("kU4T")
                .groupNumber(3)
                .departmentId("1347542347586637824")
                .build();

        // 调用仓库方法获取 Flux<User>
        var userFlux = userRepository.findByRoleAndGroupNumber(
                user.getDepartmentId(),
                user.getRole(),
                user.getGroupNumber()
        );

        // 使用 StepVerifier 验证响应式流
        StepVerifier.create(userFlux)
                .thenConsumeWhile(foundUser -> {
                    // 记录找到的用户信息
                    log.debug("Found user: {}", foundUser.toString());
                    // 可以添加更多的断言来验证用户信息
                    return true;
                })
                .verifyComplete();
    }
}