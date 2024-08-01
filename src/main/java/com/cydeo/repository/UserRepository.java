package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN TRUE ELSE FALSE END FROM User u WHERE u.role.description = 'Admin' AND u.company.id = :companyId")
    boolean isOnlyAdminInCompany(@Param("companyId") Long companyId);

    List<User> findByCompany_Id(Long companyId);
    List<User> findAllByRoleDescription(String roleDescription);

    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = :status WHERE u.company.id = :companyId")
    void updateUserStatusByCompanyId(Long companyId, boolean status);

}
