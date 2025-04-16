package com.example.webfluxr2dbcexamples.filter;

import com.example.webfluxr2dbcexamples.component.JWTComponent;
import com.example.webfluxr2dbcexamples.exception.Code;
import com.example.webfluxr2dbcexamples.exception.XException;
import com.example.webfluxr2dbcexamples.vo.RequestAttributeConstant;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class LoginFilter implements WebFilter {

    private final PathPattern includes = new PathPatternParser().parse("/api/**");
    private final List<PathPattern> excludesS = List.of(new PathPatternParser().parse("/api/login"));

    private final JWTComponent jwtComponent;
    private final ResponseHelper responseHelper;


    /*
     *ServerWebExchange 是 Spring WebFlux 中的一个核心接口，
     * 代表了一个服务器端的 HTTP 请求 - 响应交互。通过 exchange 对象，可以获取请求信息、设置响应信息等。
     *WebFilterChain chain：WebFilterChain 表示过滤器链，用于将请求传递给下一个过滤器或最终的请求处理器。
     * */
    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //request.getPath().pathWithinApplication() 用于获取请求在应用程序内部的路径
        if (!includes.matches(request.getPath().pathWithinApplication())) {
            return chain.filter(exchange);
        }
        for (PathPattern p : excludesS) {
            if (p.matches(request.getPath().pathWithinApplication())) {
                return chain.filter(exchange);
            }
        }
        String token = request.getHeaders().getFirst(RequestAttributeConstant.TOKEN);
        if (token == null) {
            return responseHelper.response(Code.UNAUTHORIZED, exchange);
        }

        /*
        * 将 JWT 中的用户 ID、角色、部门 ID 和组号等信息提取出来，并存储到 ServerWebExchange 的属性
        * */
        return jwtComponent.decode(token)
                .flatMap(decode -> {
                    exchange.getAttributes().put(RequestAttributeConstant.UID, decode.getClaim(RequestAttributeConstant.UID).asString());
                    exchange.getAttributes().put(RequestAttributeConstant.ROLE, decode.getClaim(RequestAttributeConstant.ROLE).asString());
                    exchange.getAttributes().put(RequestAttributeConstant.DEPARTMENT_ID, decode.getClaim(RequestAttributeConstant.DEPARTMENT_ID).asString());
                    if (!decode.getClaim(RequestAttributeConstant.GROUP_NUMBER).isMissing()) {
                        exchange.getAttributes().put(RequestAttributeConstant.GROUP_NUMBER, decode.getClaim(RequestAttributeConstant.GROUP_NUMBER).asInt());
                    }
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> responseHelper.response(((XException) e).getCode(), exchange));

        //
        /*DecodedJWT decode = jwtComponent.decode(token);
        exchange.getAttributes().put(RequestAttributeConstant.UID, decode.getClaim(RequestAttributeConstant.UID).asString());
        exchange.getAttributes().put(RequestAttributeConstant.ROLE, decode.getClaim(RequestAttributeConstant.ROLE).asString());

        if (!decode.getClaim(RequestAttributeConstant.GROUP_NUMBER).isMissing()) {
            exchange.getAttributes().put(RequestAttributeConstant.GROUP_NUMBER, decode.getClaim(RequestAttributeConstant.GROUP_NUMBER).asInt());
        }
        return chain.filter(exchange);*/

    }
}
