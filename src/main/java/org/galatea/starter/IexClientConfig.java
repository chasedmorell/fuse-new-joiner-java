package org.galatea.starter;

import org.springframework.context.annotation.Bean;

public class IexClientConfig {
    @Bean
    public IexClientInterceptor iexClientInterceptor() {
        return new IexClientInterceptor();
    }
}
