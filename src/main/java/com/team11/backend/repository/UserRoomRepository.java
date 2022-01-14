package com.team11.backend.repository;

import com.team11.backend.model.Room;
import com.team11.backend.model.User;
import com.team11.backend.model.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoomRepository extends JpaRepository<UserRoom,Long> {
    UserRoom findByRoomAndUser(Room room, User user);
    UserRoom findByRoomAndUserAndToUser(Room room, User user,User toUser);
    List<UserRoom> findByUser(User user);
    List<UserRoom> findByRoom(Room room);
}
