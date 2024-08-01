package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
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

    @Enumerated(EnumType.STRING)// with this annotation it is persisted as a string in the database
    private CompanyStatus companyStatus;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL) //Lazy: it will be fetched only when accessed, avoid unnecessary data loading; cascade.all: if company's persisted, address's persisted automatically
    @JoinColumn (name = "address_id") // Specifies the foreign key column (address_id) in the companies table that references the primary key of the Address entity
    private Address address;

}
