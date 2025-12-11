package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.Chat;
import com.techup.spring.spring_be.domain.ChattingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Page<Chat> findByRoomOrderByCreatedAtDesc(ChattingRoom room, Pageable pageable);
}