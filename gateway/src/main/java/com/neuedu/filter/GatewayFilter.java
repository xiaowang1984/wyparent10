package com.neuedu.filter;


import com.alibaba.fastjson.JSONObject;
import com.neuedu.config.UrlIgnored;
import com.neuedu.util.JwtUtil;
import com.neuedu.util.ResultCode;
import com.neuedu.util.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class GatewayFilter implements GlobalFilter,Ordered {
    @Resource
    UrlIgnored urlIgnored;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-type","application/json;charset=UTF-8");
        // 如果是OPTION请求 直接放行
        if(request.getMethod().name().equals("OPTION")) {
            return chain.filter(exchange);
        }
        // 如果在白名单中 放行
        List<String> urls = urlIgnored.getUrls();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : urls) {
           if(pathMatcher.match(url,request.getPath().value())) {
               return chain.filter(exchange);
           }
        }
        // 先从参数中拿 如果拿不到 从 headers中拿
        List<String> tokenList = request.getQueryParams().get("token") != null ? request.getQueryParams().get("token") : request.getHeaders().get("token");
        if(tokenList == null) {
            // 如果从哪都拿不到 未认证的非法请求
            DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(ResultData.failed(ResultCode.UNAUTHENTICATION)).getBytes(Charset.forName("UTF-8")));
            return response.writeWith(Flux.just(dataBuffer));
        }
        // 如果能拿到 解析
        try {
            JwtUtil jwtUtil = JwtUtil.deToken(tokenList.get(0));
            // 在此 还可以验证权限 这里忽略
            // 验证时间是否过期
            Date now = Calendar.getInstance().getTime();
            // 如果过期 返回tonken过期
            if(now.getTime() - jwtUtil.getTimestamp().getTime() > 0) {
                DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(ResultData.failed(ResultCode.UNAUTHENTICATION,"请求已经过期")).getBytes(Charset.forName("UTF-8")));
                return response.writeWith(Flux.just(dataBuffer));
            } else {
                // 把用户id 放进参数 通过验证
                URI uri = request.getURI();
                StringBuilder builder = new StringBuilder();
                String oldparams = uri.getRawQuery();
                if(StringUtils.isNotBlank(oldparams))
                    builder.append(oldparams).append("&");
                builder.append("userid=" + jwtUtil.getUserid());
                URI newURI = UriComponentsBuilder.fromUri(uri)
                        .replaceQuery(builder.toString())
                        .build(true)
                        .toUri();
                ServerHttpRequest newRequest = request.mutate().uri(newURI).build();
                return chain.filter(exchange.mutate().request(newRequest).build());
            }
        }catch(Exception ex) {
            // 如果解析出错 非法请求
            DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(ResultData.failed(ResultCode.UNAUTHENTICATION,"非法参数")).getBytes(Charset.forName("UTF-8")));
            return response.writeWith(Flux.just(dataBuffer));
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
