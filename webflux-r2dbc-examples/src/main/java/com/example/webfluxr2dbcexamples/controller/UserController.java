package com.example.webfluxr2dbcexamples.controller;


import com.example.webfluxr2dbcexamples.service.UserService;
import com.example.webfluxr2dbcexamples.vo.RequestAttributeConstant;
import com.example.webfluxr2dbcexamples.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {
    private  final  UserService userService;

    @GetMapping("processes")
    public Mono<ResultVO> getProcesses(@RequestAttribute(RequestAttributeConstant.DEPARTMENT_ID) String depid) {
        return  userService.listProcesses(depid).map(ResultVO::success);

    }
}
