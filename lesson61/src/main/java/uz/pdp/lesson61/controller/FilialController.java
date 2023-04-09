package uz.pdp.lesson61.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson61.entity.Filial;
import uz.pdp.lesson61.payload.ApiResponse;
import uz.pdp.lesson61.payload.FilialDto;
import uz.pdp.lesson61.payload.FilialDtoUpdate;
import uz.pdp.lesson61.service.FilialService;

import java.util.List;

@RestController
@RequestMapping("/api/filial")
public class FilialController {

    @Autowired
    FilialService filialService;

    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @GetMapping
    public HttpEntity<?> getFilials(){
        List<Filial> filials = filialService.getFilials();
        return ResponseEntity.ok(filials);
    }
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @GetMapping("/{id}")
    public HttpEntity<?> getFilial(@PathVariable Integer id){
       Filial filial = filialService.getFilial(id);
       return ResponseEntity.ok(filial);
    }
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PostMapping
    public HttpEntity<?> addFilial(@RequestBody FilialDto filialDto){
        ApiResponse apiResponse = filialService.addFilial(filialDto);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PutMapping("/{id}")
    public HttpEntity<?> editFilial(@PathVariable Integer id, @RequestBody FilialDtoUpdate filialDtoUpdate){
        ApiResponse apiResponse = filialService.editFilial(id, filialDtoUpdate);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteFilial(@PathVariable Integer id){
        ApiResponse apiResponse = filialService.deleteFilial(id);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(apiResponse);
    }

}
