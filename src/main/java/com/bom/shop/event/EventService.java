package com.bom.shop.event;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository; // DB
    private final RedisTemplate<String, Object> redisTemplate; // Cache
    private final RedissonClient redissonClient; // Lock

    // 1. 목록 조회: (Cache -> DB)
    public List<Event> getEventList() {
        String cacheKey = "events:list";

        // 1) 캐시 확인 (Lettuce)
        List<Event> cachedEvents = (List<Event>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedEvents != null) {
            System.out.println("🚀 캐시에서 목록 조회");
            return cachedEvents;
        }

        // 2) DB 조회
        System.out.println("🐢 DB에서 목록 조회");
        List<Event> events = eventRepository.findAll();

        // 3) 캐시 저장 (1분간 유효)
        // 실제로는 이벤트가 추가/수정될 때 캐시를 지워주는 로직(Evict)이 필요함
        redisTemplate.opsForValue().set(cacheKey, events, 1, TimeUnit.MINUTES);

        return events;
    }

    // 2. 선착순 신청: (Redisson Lock -> DB Update)
    public String applyEvent(Long eventId, String userId) {
        // 락 이름은 이벤트 ID별로 유니크하게 생성 (lock:event:1)
        String lockKey = "lock:event:" + eventId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // A. 락 획득 시도 (3초 대기, 5초 점유)
            boolean available = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (!available) {
                return "⚠️ 접속 폭주! 다시 시도해주세요.";
            }

            // B. DB 조회 (락 안에서 조회해야 정확함)
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

            // C. 재고 확인
            if (event.getRemainCount() <= 0) {
                return "😭 마감되었습니다.";
            }

            // D. 재고 차감 및 저장
            event.decrease();
            eventRepository.save(event); // DB 업데이트

            System.out.println("🎉 " + userId + "님 신청 완료! (남은 수량: " + event.getRemainCount() + ")");
            return "🎉 신청 성공! (남은 수량: " + event.getRemainCount() + ")";

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "에러 발생";
        } finally {
            // E. 락 해제
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}