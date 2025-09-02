package com.example.hunter_point.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username; // email
    private List<String> roles;
    private Integer points; //

    public JwtResponse(String accessToken, Long id, String username, List<String> roles, Integer points) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.points = points;
}
}
