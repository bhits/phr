package gov.samhsa.c2s.phr.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

public class MultipartSupportConfig {

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignFormEncoder(){
        return new SpringFormEncoder();
    }
}
