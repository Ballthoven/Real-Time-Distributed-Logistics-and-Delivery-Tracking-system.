package com.finalyearProject.TrackingService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLocationService {
//    private final StringRedisTemplate redisTemplate;
//    private static final String LOCATION_KEY_PREFIX = "agent:location:";
//
//    public void saveLocation(String agentId, Double latitude, Double longitude) {
//        if (redisTemplate == null) {
//            log.warn("Redis not available - skipping cache for agent {}", agentId);
//            return;
//        }
//        String key = LOCATION_KEY_PREFIX + agentId;
//        String value = latitude + "," + longitude;
//        redisTemplate.opsForValue().set(key, value);
//        log.info("Saved location to Redis for agent {}: {}", agentId, value);
//    }
//
//    public String getLocation(String agentId) {
//        if (redisTemplate == null) {
//            return null;
//        }
//        String key = LOCATION_KEY_PREFIX + agentId;
//        return redisTemplate.opsForValue().get(key);
//    }
      public void saveLocation(String agentId, Double latitude, Double longitude) {
             log.warn("Redis not available - skipping cache for agent {}", agentId);
}

      public String getLocation(String agentId) {
          return null;
    }

}
