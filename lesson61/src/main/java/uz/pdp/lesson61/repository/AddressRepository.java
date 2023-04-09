package uz.pdp.lesson61.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson61.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
