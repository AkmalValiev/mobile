package uz.pdp.lesson61.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilialDto {

    @NotNull
    private String name;
    @NotNull
    private String city;
    @NotNull
    private String street;

}
