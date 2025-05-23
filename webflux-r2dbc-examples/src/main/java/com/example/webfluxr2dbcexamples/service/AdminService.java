package com.example.webfluxr2dbcexamples.service;

import com.example.webfluxr2dbcexamples.dox.Department;
import com.example.webfluxr2dbcexamples.exception.Code;
import com.example.webfluxr2dbcexamples.exception.XException;
import com.example.webfluxr2dbcexamples.repository.DepartmentRepository;
import com.example.webfluxr2dbcexamples.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Mono<Void> addDepartment(Department department) {
        return departmentRepository.save(department).then();
    }

    public Mono<List<Department>> listDepartments() {
        return departmentRepository.findAll().collectList();
    }
    @Transactional
    public Mono<Void> removeDepartment(String did) {
        return userRepository.countByDepartmentId(did)
                .flatMap(r -> {
                    if(r > 0) {
                        return Mono.error(XException.builder().codeN(Code.ERROR).message("部门包含用户禁止删除").build());
                    }
                    return departmentRepository.deleteById(did).then();
                });
    }
}
