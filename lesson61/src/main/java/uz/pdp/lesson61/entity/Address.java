package uz.pdp.lesson61.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.InnoDBStorageEngine;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

}
