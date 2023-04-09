package uz.pdp.lesson61.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson61.entity.Filial;

public interface FilialRepository extends JpaRepository<Filial, Integer> {

    boolean existsByName(String name);

    boolean existsByIdAndNameNot(Integer id, String name);

}
