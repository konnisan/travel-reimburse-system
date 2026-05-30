package com.kjd.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjd.backend.vo.TravelReimburseBaseDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Service
public class RedisSupportService {
    private static final Logger log = LoggerFactory.getLogger(RedisSupportService.class);
    private static final String BASE_DATA_KEY = "travel-reimburse:base-data:v1";
    private static final String LOGIN_FAIL_PREFIX = "travel-reimburse:login-fail:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.cache.base-data-ttl-seconds:1800}")
    private long baseDataTtlSeconds;

    @Value("${app.security.login-max-failures:5}")
    private int loginMaxFailures;

    @Value("${app.security.login-lock-seconds:300}")
    private long loginLockSeconds;

    public RedisSupportService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public TravelReimburseBaseDataVO getBaseDataCache() {
        try {
            String json = redisTemplate.opsForValue().get(BASE_DATA_KEY);
            if (!StringUtils.hasText(json)) {
                return null;
            }
            log.info("Base data cache hit: {}", BASE_DATA_KEY);
            return objectMapper.readValue(json, TravelReimburseBaseDataVO.class);
        } catch (Exception ex) {
            log.warn("Read base data cache failed, fallback to database/static data.", ex);
            return null;
        }
    }

    public void setBaseDataCache(TravelReimburseBaseDataVO data) {
        if (data == null) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(
                    BASE_DATA_KEY,
                    objectMapper.writeValueAsString(data),
                    Duration.ofSeconds(baseDataTtlSeconds)
            );
            log.info("Base data cached: {}, ttl={}s", BASE_DATA_KEY, baseDataTtlSeconds);
        } catch (Exception ex) {
            log.warn("Write base data cache failed, continue without cache.", ex);
        }
    }

    public boolean isLoginLocked(String username, String clientIp) {
        try {
            String value = redisTemplate.opsForValue().get(loginFailKey(username, clientIp));
            return parseInt(value) >= loginMaxFailures;
        } catch (Exception ex) {
            log.warn("Check login lock failed, continue without Redis limit.", ex);
            return false;
        }
    }

    public Integer recordLoginFailure(String username, String clientIp) {
        try {
            String key = loginFailKey(username, clientIp);
            Long failures = redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, Duration.ofSeconds(loginLockSeconds));
            int currentFailures = failures == null ? 1 : failures.intValue();
            return Math.max(loginMaxFailures - currentFailures, 0);
        } catch (Exception ex) {
            log.warn("Record login failure failed, continue without Redis limit.", ex);
            return null;
        }
    }

    public void clearLoginFailures(String username, String clientIp) {
        try {
            redisTemplate.delete(loginFailKey(username, clientIp));
        } catch (Exception ex) {
            log.warn("Clear login failure counter failed.", ex);
        }
    }

    public int getLoginMaxFailures() {
        return loginMaxFailures;
    }

    public long getLoginLockSeconds() {
        return loginLockSeconds;
    }

    private String loginFailKey(String username, String clientIp) {
        String safeUsername = StringUtils.hasText(username) ? username.trim().toLowerCase() : "anonymous";
        String safeIp = StringUtils.hasText(clientIp) ? clientIp.trim() : "unknown";
        return LOGIN_FAIL_PREFIX + safeUsername + ":" + safeIp;
    }

    private int parseInt(String value) {
        if (!StringUtils.hasText(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
