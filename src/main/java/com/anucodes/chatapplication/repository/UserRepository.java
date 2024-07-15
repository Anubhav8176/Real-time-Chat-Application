package com.anucodes.chatapplication.repository;

import com.anucodes.chatapplication.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    public UserInfo findByUsername(String username);
}
