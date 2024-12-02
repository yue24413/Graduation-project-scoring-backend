package com.example.webfluxr2dbcexamples.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProcessRepository extends ReactiveCrudRepository<Process, String> {
    Flux<Process> findByDepartmentId(String depid);
    Mono<Process> findByDepartmentIdAndId(String depid, String id);

    @Modifying
    Mono<Integer> deleteByDepartmentIdAndId(String depid,String id);


}
