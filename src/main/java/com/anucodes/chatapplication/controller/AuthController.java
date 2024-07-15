package com.anucodes.chatapplication.controller;

import com.anucodes.chatapplication.entities.RefreshToken;
import com.anucodes.chatapplication.models.UserInfoDto;
import com.anucodes.chatapplication.response.JwtResponseDTO;
import com.anucodes.chatapplication.services.JwtService;
import com.anucodes.chatapplication.services.RefreshTokenServices;
import com.anucodes.chatapplication.services.UserDetailsServiceImple;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenServices refreshTokenServices;

    @Autowired
    private UserDetailsServiceImple userDetailsService;

    @PostMapping("/api/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfoDto){
        try {
            Boolean isSignUp = userDetailsService.signUp(userInfoDto);
            if (Boolean.FALSE.equals(isSignUp))
                return new ResponseEntity<>("Already Exist!", HttpStatus.BAD_REQUEST);

            RefreshToken refreshToken = refreshTokenServices.generateRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(
                    JwtResponseDTO
                            .builder()
                            .accessToken(jwtToken)
                            .token(refreshToken.getToken())
                            .build(),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to SignUp!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
