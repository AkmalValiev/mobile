package uz.pdp.lesson61.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    private String password;

}
