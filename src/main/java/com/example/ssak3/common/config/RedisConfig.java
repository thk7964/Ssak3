package com.example.ssak3.common.config;

import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.usercoupon.model.response.CouponListForUserGetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, PageResponse<CouponListForUserGetResponse>> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, PageResponse<CouponListForUserGetResponse>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);


        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<PageResponse> serializer = new Jackson2JsonRedisSerializer<>(mapper, PageResponse.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}