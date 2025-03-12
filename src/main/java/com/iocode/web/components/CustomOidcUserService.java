package com.iocode.web.components;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // ✅ Delegate to the default OidcUserService to load the user
        OidcUser oidcUser = super.loadUser(userRequest);

        // ✅ Extract roles/authorities from the token
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        // 🔹 Extract 'realm_access.roles'
        Map<String, Object> realmAccess = oidcUser.getAttribute("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            roles.forEach(role -> mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        }

        // 🔹 Extract 'resource_access.spring_oauth2.roles'
        Map<String, Object> resourceAccess = oidcUser.getAttribute("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey("spring_oauth2")) {
            Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("spring_oauth2");
            if (clientRoles.containsKey("roles")) {
                List<String> roles = (List<String>) clientRoles.get("roles");
                roles.forEach(role -> mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
            }
        }

        // 🔹 Extract 'authorities' claim if available
        List<String> authoritiesClaim = oidcUser.getAttribute("authorities");
        if (authoritiesClaim != null) {
            authoritiesClaim.forEach(auth -> mappedAuthorities.add(new SimpleGrantedAuthority(auth.toUpperCase())));
        }

        // ✅ Create a new OidcUser with updated authorities
        return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
