package com.common.gmt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.ZoneId;

@Configuration
public class GmtZoneConfiguration {

    @Bean
    public ZoneId zoneId(Environment environment) {
        return ZoneId.of(environment.getProperty("zoneid", "America/Sao_Paulo"));
    }

}
