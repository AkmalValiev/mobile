package uz.pdp.lesson61.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilialDtoUpdate {

    @NotNull
    private String name;

    @NotNull
    private Integer id;

}
