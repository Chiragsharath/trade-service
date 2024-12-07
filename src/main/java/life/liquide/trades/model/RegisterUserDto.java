package life.liquide.trades.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class RegisterUserDto {

    private String email;

    private String password;

    private String fullName;

}
