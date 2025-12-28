package com.bom.shop.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    // 기본 CRUD 제공됨
}