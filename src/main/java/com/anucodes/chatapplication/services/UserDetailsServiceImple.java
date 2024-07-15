package com.anucodes.chatapplication.services;

import com.anucodes.chatapplication.entities.UserInfo;
import com.anucodes.chatapplication.models.UserInfoDto;
import com.anucodes.chatapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImple implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("Username is null");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signUp(UserInfoDto userInfoDto){
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if(Objects.nonNull(checkIfUserExists(userInfoDto)))return false;

        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(
                userId,
                userInfoDto.getFirstname(),
                userInfoDto.getLastname(),
                userInfoDto.getUsername(),
                userInfoDto.getPassword(),
                new HashSet<>()
        ));
        return true;
    }

    public List<UserInfo> getAllUser(){
        return userRepository.findAll();
    }

}
