package com.example.webfluxr2dbcexamples.filter;

import com.example.webfluxr2dbcexamples.exception.Code;
import com.example.webfluxr2dbcexamples.vo.ResultVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseHelper {
    //ObjectMapper 是 Jackson 库中的一个核心类，它主要用于 Java 对象和 JSON 数据之间的相互转换。
    private final ObjectMapper objectMapper;
    /*
    * @SneakyThrows：这是 Lombok 提供的注解，它允许方法抛出受检查异常而无需在方法签名中显式声明。在这个方法中，
    * 可能会抛出与 JSON 序列化相关的异常，使用该注解可以避免在方法签名中声明这些异常，使代码更加简洁。
    * */
    @SneakyThrows
    public Mono<Void> response(Code code, ServerWebExchange exchange) {
        byte[] bytes = objectMapper.writeValueAsString(ResultVO.error(code)) //使用 ObjectMapper 将 ResultVO 实例转换为 JSON 字符串
                .getBytes(StandardCharsets.UTF_8); // 将 JSON 字符串转换为 UTF - 8 编码的字节数组
        ServerHttpResponse response = exchange.getResponse();

        //response.bufferFactory()：获取 ServerHttpResponse 的数据缓冲区工厂，用于创建 DataBuffer 对象。
        //wrap(bytes)：使用缓冲区工厂将 字节数组 包装成一个 DataBuffer 对象
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        response.getHeaders().add("Content-Type", "application/json");
        return response.writeWith(Flux.just(wrap));
    }
}