package com.example.webfluxr2dbcexamples.service;
import com.example.webfluxr2dbcexamples.dox.Process;
import com.example.webfluxr2dbcexamples.dox.ProcessScore;
import com.example.webfluxr2dbcexamples.dox.User;
import com.example.webfluxr2dbcexamples.repository.ProcessRepository;
import com.example.webfluxr2dbcexamples.repository.ProcessScoreRepository;
import com.example.webfluxr2dbcexamples.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {
    private final ProcessRepository processRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProcessScoreRepository processScoreRepository;

    @Transactional
    public Mono<Void> addProcess(Process process) {
        return processRepository.save(process).then();
    }

    public Mono<Void> removeProcess(String pid,String depid) {
        return processRepository.deleteByIdAndDepartmentId(pid, depid).then();
    }

    @Transactional
    public Mono<Void> updateProcess(Process process, String pid, String depid) {
        return processRepository.findByIdAndDepartmentId(process.getId(),depid)
                .flatMap(p -> {
                    p.setAuth(process.getAuth());
                    p.setItems(process.getItems());
                    p.setName(process.getName());
                    p.setPoint(process.getPoint());
                    p.setStudentAttach(process.getStudentAttach());
                    return processRepository.save(p);
                }).then();
    }

    public Mono<List<ProcessScore>> ListProcessScores(String pid,int groupNumber) {
        return processScoreRepository.findByGroup(groupNumber,pid).collectList();
    }

    public Mono<List<ProcessScore>> listProcessScores(int groupNumber) {
        return processScoreRepository.findByGroup(groupNumber).collectList();
    }

    @Transactional
    public Mono<Integer> updatePassword(String account) {
        return userRepository.updatePasswordByAccount(account, passwordEncoder.encode(account));
    }

    @Transactional
    public Mono<Void> addUsers(List<User> users,String role,String departmentId) {
        for (User user : users) {
            user.setDepartmentId(departmentId);
            user.setPassword(passwordEncoder.encode(user.getAccount()));
       ;     user.setRole(role);
        }
        return userRepository.saveAll(users).then();
    }

    @Transactional
    public Mono<Void> updateStudents(List<User> users,String role) {
        return Flux.fromIterable(users)
                .flatMap(user ->
                        userRepository.findByAccount(user.getAccount()))
                .flatMap(user ->{
                    if(user.getGroupNumber() != null) {
                        user.setGroupNumber(user.getGroupNumber());
                    }
                    if(user.getStudent() != null) {
                        user.setStudent(user.getStudent());
                    }
                    return userRepository.save(user);
                })
                .then();
    }

    public Mono<User> getUser(String account,String departmentId) {
        return userRepository.findByAccountAndDepartmentId(account,departmentId);
    }

}
