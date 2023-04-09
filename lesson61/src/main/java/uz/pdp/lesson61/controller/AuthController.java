package uz.pdp.lesson61.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson61.entity.User;
import uz.pdp.lesson61.payload.ApiResponse;
import uz.pdp.lesson61.payload.LoginDto;
import uz.pdp.lesson61.payload.RegisterDto;
import uz.pdp.lesson61.payload.UpdateDto;
import uz.pdp.lesson61.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = authService.register(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @PutMapping("/update/{username}")
    public HttpEntity<?> editUser(@PathVariable String username, @RequestBody UpdateDto updateDto){
        ApiResponse apiResponse = authService.editUser(updateDto);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getUsers(){
        List<User> users = authService.getUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{username}")
    public HttpEntity<?> deleteUser(@PathVariable String username){
        ApiResponse apiResponse = authService.deleteUser(username);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

}
