package com.team11.backend.repository;



import com.team11.backend.model.Post;
import com.team11.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team11.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByPost(Post post);
    Optional<Room> findByRoomNameAndPost_Id(String roomName, Long postId);
    Optional<Room> findByRoomName(String roomName);
}
