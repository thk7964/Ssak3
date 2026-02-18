package com.example.ssak3.domain.usercoupon.service;

import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.usercoupon.model.response.CouponListForUserGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserCouponCacheService {

    private static final String USER_COUPON_LIST_CACHE_PREFIX = "coupons:user:page:";
    private final RedisTemplate<String, PageResponse<CouponListForUserGetResponse>> couponRedisTemplate;

    /**
     * 사용자용 쿠폰 목록 캐시 저장
     */
    public void saveUserCouponListCache(int pageNumber, int pageSize, PageResponse<CouponListForUserGetResponse> data) {
        String key = USER_COUPON_LIST_CACHE_PREFIX + pageNumber + ":size:" + pageSize;
        couponRedisTemplate.opsForValue().set(key, data, 10, TimeUnit.MINUTES);
    }

    /**
     * 사용자용 쿠폰 목록 캐시 조회
     */
    public PageResponse<CouponListForUserGetResponse> getUserCouponListCache(int pageNumber, int pageSize) {
        String key = USER_COUPON_LIST_CACHE_PREFIX + pageNumber + ":size:" + pageSize;
        return couponRedisTemplate.opsForValue().get(key);
    }

    /**
     * 쿠폰 정보 수정 or 삭제 시 사용자용 캐시도 비우기
     */
    public void clearUserCouponListCache() {
        var keys = couponRedisTemplate.keys(USER_COUPON_LIST_CACHE_PREFIX + "*");
        couponRedisTemplate.delete(keys);
    }
}