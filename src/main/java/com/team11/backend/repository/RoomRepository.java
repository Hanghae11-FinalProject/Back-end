package com.team11.backend.repository;


import com.team11.backend.model.Post;
import com.team11.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByPost(Post post);
}
