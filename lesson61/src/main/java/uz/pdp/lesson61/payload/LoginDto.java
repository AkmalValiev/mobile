package uz.pdp.lesson61.payload;

import jakarta.validation.constraints.NotNull;

public class LoginDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
