package org.galatea.starter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class IexClientInterceptor implements RequestInterceptor {

    @Value("${spring.rest.iexToken}")
    String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.query("token",token);
    }
}
