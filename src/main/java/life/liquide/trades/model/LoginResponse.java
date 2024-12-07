package life.liquide.trades.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class LoginResponse {
    private String token;

    private long expiresIn;



}


