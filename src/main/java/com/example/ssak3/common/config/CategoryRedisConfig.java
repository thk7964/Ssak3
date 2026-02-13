package com.example.ssak3.common.config;

import com.example.ssak3.domain.category.model.response.CategoryListGetResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;


@Configuration
public class CategoryRedisConfig {

    @Primary
    @Bean
    public CacheManager categoryCacheManager(RedisConnectionFactory connectionFactory) {

        // Serializer 에게 줄 규칙 정의
        ObjectMapper objectMapper = new ObjectMapper();

        // 날짜/시간 모듈 등록 및 타임스탬프 형식 비활성화
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 1. Jackson의 ObjectMapper에게 "이건 List인데 안에 CategoryGetResponse가 들어있어" 라고 말해줌
        JavaType categoryListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, CategoryListGetResponse.class);

        // 2. 그 정보를 시리얼라이저에 넘겨줌
        Jackson2JsonRedisSerializer<List<CategoryListGetResponse>> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, categoryListType);

        // 3. Redis 캐시 설정 구성
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // null 값은 캐싱하지 않음
                .disableCachingNullValues()
                // 캐시 유효 기간 설정 (30분)
                .entryTtl(Duration.ofMinutes(30))
                // 키와 값의 직렬화 방식 설정 (아까 배운 JSON 방식 적용!)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

}
