# 13) Redis로 캐싱 + 동시성 처리 (선착순 이벤트)

## 의존성 (build.gradle)
```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.redisson:redisson-spring-boot-starter:3.31.0'
```

## 목표

- **목록 조회**: Redis 캐시 → 없으면 DB 조회 후 캐싱
- **선착순 신청**: Redisson **분산락**으로 동시성 제어 후 DB 재고 차감

---

## 1) 목록 캐싱 (RedisTemplate)

- 캐시 키: `events:list`
- TTL: 1분

```java
public List<Event>getEventList() {
String key="events:list";

    List<Event> cached = (List<Event>) redisTemplate.opsForValue().get(key);
if (cached !=null) return cached;

    List<Event> events = eventRepository.findAll();
    redisTemplate.opsForValue().set(key, events,1, TimeUnit.MINUTES);
return events;
}
```

---

## 2) 선착순 동시성 처리 (Redisson Lock)

- 락 키: `lock:event:{eventId}`
- 락 획득 후에만 DB 조회/차감(중요)

```java
public String applyEvent(Long eventId, String userId) {
    RLock lock= redissonClient.getLock("lock:event:" + eventId);

    try {
        booleanok= lock.tryLock(3,5, TimeUnit.SECONDS);
        if (!ok) return"접속 폭주, 재시도";
        Event event= eventRepository.findById(eventId)
                .orElseThrow(() ->newIllegalArgumentException("이벤트 없음"));

        if (event.getRemainCount() <=0)return"마감";

        event.decrease();
        eventRepository.save(event);
        return "성공 (남은 수량: " + event.getRemainCount() +")";

    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return "에러";
    } finally {
        if (lock.isLocked() && lock.isHeldByCurrentThread()) lock.unlock();
    }
}
```

---

## 3) 프론트 호출 (AJAX)

```jsx
fetch(`/apply?eventId=${eventId}&userId=${userId}`).then(r => r.text());

```

## 동시성 테스트(간단)

```jsx
for (let i =0; i <100; i++) {
fetch(`/apply?eventId=1&userId=tester_${i}`);
}
```