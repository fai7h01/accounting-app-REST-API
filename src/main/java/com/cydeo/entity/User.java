package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String phone;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private Boolean accountNonLocked;
}

