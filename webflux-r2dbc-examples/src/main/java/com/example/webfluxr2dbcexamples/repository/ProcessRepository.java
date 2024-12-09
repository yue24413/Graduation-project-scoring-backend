package com.example.webfluxr2dbcexamples.repository;
import com.example.webfluxr2dbcexamples.dox.Process;
import com.example.webfluxr2dbcexamples.dox.ProcessScore;
import com.example.webfluxr2dbcexamples.dox.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProcessRepository extends ReactiveCrudRepository<Process, String> {
    Flux<Process> findByDepartmentId(String depid);

    Mono<Process> findByIdAndDepartmentId(String id, String depid);

    @Modifying
    Mono<Integer> deleteByIdAndDepartmentId(String id, String depid);
}
