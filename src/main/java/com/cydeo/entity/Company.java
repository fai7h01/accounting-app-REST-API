package com.cydeo.entity;

import com.cydeo.enums.CompanyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "companies")
@Where(clause = "is_deleted=false")
public class Company extends BaseEntity {

    @Column(unique = true, nullable = false)// unique title, title cannot be null
    private String title;

    private String phone;

    private String website;

    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

    @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn (name = "address_id")
    private Address address;

}
