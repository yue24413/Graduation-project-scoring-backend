package com.example.webfluxr2dbcexamples.repository;

import com.example.webfluxr2dbcexamples.dox.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends ReactiveCrudRepository<Department, String> {
}
