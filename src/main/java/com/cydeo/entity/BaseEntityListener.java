package com.cydeo.entity;

import com.cydeo.service.KeycloakService;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    private final KeycloakService keycloakService;

    public BaseEntityListener(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity){

        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        baseEntity.setInsertUserId(keycloakService.getLoggedInUser().getId());
        baseEntity.setLastUpdateUserId(keycloakService.getLoggedInUser().getId());

    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity){

        baseEntity.setLastUpdateDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateUserId(keycloakService.getLoggedInUser().getId());

    }


}
