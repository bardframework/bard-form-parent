package org.bardframework.form;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class BardFormTestConfiguration {

    /**
     * در جکسون ۳ نگاشت‌گرها تغییرناپذیرند و تنظیمات باید هنگام ساخت اعمال شود.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }
}
