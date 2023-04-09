package uz.pdp.lesson61.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson61.entity.Role;
import uz.pdp.lesson61.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);

}
