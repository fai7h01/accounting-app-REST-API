package com.cydeo.service.impl;

import com.cydeo.config.KeycloakProperties;
import com.cydeo.dto.UserDto;
import com.cydeo.exception.UserDoesNotExistsException;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.UserService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.Arrays.asList;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakProperties keycloakProperties;
    private final UserService userService;

    public KeycloakServiceImpl(KeycloakProperties keycloakProperties, UserService userService) {
        this.keycloakProperties = keycloakProperties;
        this.userService = userService;
    }

    @Override
    public Response userCreate(UserDto userDTO) {

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDTO.getPassword());

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDTO.getUsername());
        keycloakUser.setFirstName(userDTO.getFirstname());
        keycloakUser.setLastName(userDTO.getLastname());
        keycloakUser.setEmail(userDTO.getUsername());
        keycloakUser.setCredentials(asList(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);


        Keycloak keycloak = getKeycloakInstance();

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        // Create Keycloak user
        Response result = usersResource.create(keycloakUser);

        String userId = getCreatedId(result);
        ClientRepresentation appClient = realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId())
                .roles().get(userDTO.getRole().getDescription()).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                .add(asList(userClientRole));


        keycloak.close();
        return result;
    }

    @Override
    public void delete(String userName) {

        Keycloak keycloak = getKeycloakInstance();

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> userRepresentations = usersResource.search(userName);
        String uid = userRepresentations.get(0).getId();
        usersResource.delete(uid);

        keycloak.close();
    }

    @Override
    public UserDto getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();
        return userService.findByUsername(username);
    }

    private Keycloak getKeycloakInstance(){
        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser()
                , keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
    }
}