package uz.pdp.lesson61.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Set<Integer> idRoles;

    private Integer filialId;

    private boolean accountNonExpired=true;
    private boolean accountNonLocked=true;
    private boolean credentialsNonExpired=true;
    private boolean enabled=true;

}
