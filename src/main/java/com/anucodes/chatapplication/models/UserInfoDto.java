package com.anucodes.chatapplication.models;

import com.anucodes.chatapplication.entities.UserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {
    private String firstName;
    private String lastName;
    private String username;
}
