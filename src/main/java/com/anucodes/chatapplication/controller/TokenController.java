package com.anucodes.chatapplication.controller;

import com.anucodes.chatapplication.entities.RefreshToken;
import com.anucodes.chatapplication.entities.UserInfo;
import com.anucodes.chatapplication.request.AuthRequestDTO;
import com.anucodes.chatapplication.request.RefreshTokenDTO;
import com.anucodes.chatapplication.response.JwtResponseDTO;
import com.anucodes.chatapplication.services.JwtService;
import com.anucodes.chatapplication.services.RefreshTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private RefreshTokenServices refreshTokenServices;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        System.out.println(authentication.isAuthenticated());
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenServices.generateRefreshToken(authRequestDTO.getUsername());
            System.out.println(refreshToken.toString());
            return new ResponseEntity<>(JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO){

    return refreshTokenServices
            .findByToken(refreshTokenDTO.getToken())
            .map(refreshTokenServices::verifyExpiration)
            .map(RefreshToken::getUserInfo)
            .map(userInfo -> {
                String accessToken = jwtService.generateToken(userInfo.getUsername());
                return JwtResponseDTO
                        .builder()
                        .accessToken(accessToken)
                        .token(refreshTokenDTO.getToken())
                        .build();
            }).orElseThrow(()-> new RuntimeException("No refresh token found in DB"));
    }
}
