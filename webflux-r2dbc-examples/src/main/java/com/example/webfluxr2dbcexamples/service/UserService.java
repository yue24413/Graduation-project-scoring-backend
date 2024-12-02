package com.example.webfluxr2dbcexamples.service;

import com.example.webfluxr2dbcexamples.dox.User;
import com.example.webfluxr2dbcexamples.repository.ProcessRepository;
import com.example.webfluxr2dbcexamples.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProcessRepository processRepository;

    public Mono<User> getUserByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    public Mono<List<User>> listStudents(String tid,String depid) {
        return userRepository.findStudentByTeacherId(tid,depid).collectList();
    }
    public Mono<List<User>> listUsers(String role, String depid) {
        return userRepository.findByRoleAndDepartmentIdOrderById(depid, role).collectList();
    }


    public Mono<List<User>> listUsers(String role, int groupNumber, String depid) {
        return userRepository.findByRoleAndGroupNumber(depid, role, groupNumber).collectList();
    }

    @Transactional
    public Mono<Void> updatePassword(String uid, String password) {
        return userRepository.updatePasswordByAccount(uid,passwordEncoder.encode(password)).then();
    }

    public Mono<List<Process>> listProcesses(String depid) {
        return processRepository.findByDepartmentId(depid).collectList();
    }

}
