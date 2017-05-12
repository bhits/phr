package gov.samhsa.c2s.phr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhrApplication.class, args);
    }
}
