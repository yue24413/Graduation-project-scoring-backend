package com.example.webfluxr2dbcexamples.dox;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Process {
    public static final String TUTOR = "AsImV";
    public static final String REVIEW = "zg0NS";
    @Id
    @CreatedBy
    private String id;
    private String name;
    private String description;
    private String auth;
    private String departmentId;
    private int point;
    private String studentAttach;
    @ReadOnlyProperty
    @JsonIgnore
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    @JsonIgnore
    private LocalDateTime updateTime;


}
