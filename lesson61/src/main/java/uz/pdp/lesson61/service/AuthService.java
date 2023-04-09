package uz.pdp.lesson61.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson61.entity.Filial;
import uz.pdp.lesson61.entity.Role;
import uz.pdp.lesson61.entity.User;
import uz.pdp.lesson61.entity.enums.RoleName;
import uz.pdp.lesson61.payload.ApiResponse;
import uz.pdp.lesson61.payload.LoginDto;
import uz.pdp.lesson61.payload.RegisterDto;
import uz.pdp.lesson61.payload.UpdateDto;
import uz.pdp.lesson61.repository.FilialRepository;
import uz.pdp.lesson61.repository.RoleRepository;
import uz.pdp.lesson61.repository.UserRepository;
import uz.pdp.lesson61.security.JwtProvider;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    FilialRepository filialRepository;

    public ApiResponse register(RegisterDto registerDto) {
        boolean existsByUsername = userRepository.existsByUsername(registerDto.getUsername());
        if (existsByUsername)
            return new ApiResponse("Bunday username mavjud!", false);
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFilial(null);
        user.setEnabled(true);
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.USER)));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        userRepository.save(user);
        return new ApiResponse("User qo'shildi!", true);
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse(token, true);
        }catch (Exception e){
            return new ApiResponse("Login yoki parol xato!", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User topilmadi"));
    }

    public ApiResponse editUser(UpdateDto updateDto) {

        Optional<User> optionalUser = userRepository.findByUsername(updateDto.getUsername());
        if (!optionalUser.isPresent())
            return new ApiResponse("Kiritilgan username bo'yicha user topilmadi!", false);
        User user = optionalUser.get();

        if (updateDto.getUsername()!=null) {
            boolean existsByIdAndUsernameNot = userRepository.existsByIdAndUsernameNot(user.getId(), updateDto.getUsername());
            if (existsByIdAndUsernameNot)
                return new ApiResponse("Bunday username li user majjud!", false);
            user.setUsername(updateDto.getUsername());
        }

        if (updateDto.getFirstName()!=null){
            user.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName()!=null){
            user.setLastName(updateDto.getLastName());
        }
        if (updateDto.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) authentication.getPrincipal();
        boolean director = false, manager = false, filialManager = false, employeeManager = false, filialDirector = false;

        Set<Role> roles = user1.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equals("DIRECTOR"))
                director = true;
            if (role.getRoleName().equals("MANAGER"))
                manager = true;
            if (role.getRoleName().equals("FILIAL_MANAGER"))
                filialManager = true;
            if (role.getRoleName().equals("EMPLOYEE_MANAGER"))
                employeeManager = true;
            if (role.getRoleName().equals("FILIAL_DIRECTOR"))
                filialDirector = true;
        }

        Set<Role> roles1 = new HashSet<>();

        Set<Integer> idRoles = updateDto.getIdRoles();
        for (Integer idRole : idRoles) {
            Optional<Role> optionalRole = roleRepository.findById(idRole);
            if (optionalRole.isPresent()){
                roles1.add(optionalRole.get());
            }else {
                return new ApiResponse("Kiritilgan id bo'yicha role topilmadi!", false);
            }
        }

        if (updateDto.getIdRoles()!=null && director){
            for (Role role : roles1) {
                if (role.getRoleName().equals(RoleName.DIRECTOR)){
                    return new ApiResponse("Director qo'shish mumkin emas!", false);
                }
            }
            user.setRoles(roles1);
        }else if (updateDto.getIdRoles()!=null && manager){
            for (Role role : roles1) {
                if (role.getRoleName().equals(RoleName.DIRECTOR) || role.getRoleName().equals(RoleName.MANAGER)){
                    return new ApiResponse("Director va manager qo'shish mumkin emas!", false);
                }
            }
            user.setRoles(roles1);
        }else if (updateDto.getIdRoles()!=null && filialManager){
            for (Role role : roles1) {
                if (role.getRoleName().equals(RoleName.DIRECTOR)
                        || role.getRoleName().equals(RoleName.MANAGER)
                || role.getRoleName().equals(RoleName.FILIAL_MANAGER)){
                    return new ApiResponse("Director, manager va filial_manager qo'shish mumkin emas!", false);
                }
            }
            user.setRoles(roles1);
        }else if (updateDto.getIdRoles()!=null && employeeManager){
            for (Role role : roles1) {
                if (role.getRoleName().equals(RoleName.DIRECTOR)
                        || role.getRoleName().equals(RoleName.MANAGER)
                        || role.getRoleName().equals(RoleName.FILIAL_MANAGER)
                || role.getRoleName().equals(RoleName.EMPLOYEE_MANAGER)){
                    return new ApiResponse("Director, manager, filial_manager va employee_manager qo'shish mumkin emas!", false);
                }
            }
            user.setRoles(roles1);
        }else if (updateDto.getIdRoles()!=null && filialDirector){
            for (Role role : roles1) {
                if (role.getRoleName().equals(RoleName.DIRECTOR)
                        || role.getRoleName().equals(RoleName.MANAGER)
                        || role.getRoleName().equals(RoleName.FILIAL_MANAGER)
                        || role.getRoleName().equals(RoleName.EMPLOYEE_MANAGER)
                ||role.getRoleName().equals(RoleName.FILIAL_DIRECTOR)){
                    return new ApiResponse("Director, manager, filial_manager, employee_manager va filial_director qo'shish mumkin emas!", false);
                }
            }
            user.setRoles(roles1);
        }else if (updateDto.getIdRoles()!=null){
            return new ApiResponse("Sizda role o'zgartirish imkoniyati yo'q!", false);
        }

        if (updateDto.getFilialId()!=null){
            Optional<Filial> optionalFilial = filialRepository.findById(updateDto.getFilialId());
            if (!optionalFilial.isPresent())
                return new ApiResponse("Bunday filial mavjud emas!", false);
            if (director || manager || filialManager){
                user.setFilial(optionalFilial.get());
            }
        }

        user.setEnabled(updateDto.isEnabled());
        user.setCredentialsNonExpired(updateDto.isCredentialsNonExpired());
        user.setAccountNonExpired(updateDto.isAccountNonExpired());
        user.setAccountNonLocked(updateDto.isAccountNonLocked());
        userRepository.save(user);
        return new ApiResponse("User o'zgardi!", true);
    }

    public List<User> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) authentication.getPrincipal();
        boolean director = false, manager = false, filialManager = false, employeeManager = false, filialDirector = false;

        Set<Role> roles = user1.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equals("DIRECTOR"))
                director = true;
            if (role.getRoleName().equals("MANAGER"))
                manager = true;
            if (role.getRoleName().equals("FILIAL_MANAGER"))
                filialManager = true;
            if (role.getRoleName().equals("EMPLOYEE_MANAGER"))
                employeeManager = true;
            if (role.getRoleName().equals("FILIAL_DIRECTOR"))
                filialDirector = true;
        }
        if (director || manager || filialManager || employeeManager){
            List<User> userList = userRepository.findAll();
            return userList;
        }else if (filialDirector){
            List<User> userList = userRepository.findByFilial_Id(user1.getFilial().getId());
            return userList;
        }else {
            return null;
        }
    }


    public ApiResponse deleteUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) authentication.getPrincipal();
        boolean director = false, manager = false, filialManager = false, employeeManager = false, filialDirector = false;

        Set<Role> roles = user1.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equals("DIRECTOR"))
                director = true;
            if (role.getRoleName().equals("MANAGER"))
                manager = true;
            if (role.getRoleName().equals("FILIAL_MANAGER"))
                filialManager = true;
            if (role.getRoleName().equals("EMPLOYEE_MANAGER"))
                employeeManager = true;
            if (role.getRoleName().equals("FILIAL_DIRECTOR"))
                filialDirector = true;
        }
        if (director||manager||filialManager||employeeManager){
            try {
                userRepository.deleteByUsername(username);
                return new ApiResponse("User o'chirildi!", true);
            }catch (Exception e){
                return new ApiResponse("Xatolik!", false);
            }
        }else if (filialDirector){
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (!optionalUser.isPresent())
                return new ApiResponse("User topilmadi!", false);
            User user = optionalUser.get();
            if (user.getFilial().getId()==user1.getFilial().getId()){
                try {
                    userRepository.deleteByUsername(username);
                    return new ApiResponse("User o'chirildi!", true);
                }catch (Exception e){
                    return new ApiResponse("Xatolik!", false);
                }
            }
        }
        return new ApiResponse("Sizda o'chirishga ruxsat yo'q!", false);
    }
}
