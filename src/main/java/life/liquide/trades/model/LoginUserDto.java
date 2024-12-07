package life.liquide.trades.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class LoginUserDto {

    private String email;

    private String password;

}
