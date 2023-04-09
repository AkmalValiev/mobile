package uz.pdp.lesson61.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson61.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByIdAndUsernameNot(UUID id, String username);

    List<User> findByFilial_Id(Integer filial_id);

    void deleteByUsername(String username);

}
