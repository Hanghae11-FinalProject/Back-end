package com.team11.backend.repository;

import com.team11.backend.model.Message;
import com.team11.backend.model.Room;
import com.team11.backend.model.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    PageImpl<Message> findByRoom(Room room, Pageable pageable);
    void deleteAllByRoom(Room room);

}
