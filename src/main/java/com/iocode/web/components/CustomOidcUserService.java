package com.iocode.web.components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CustomOidcUserService.class);

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // ✅ Delegate to the default OidcUserService to load the user
        OidcUser oidcUser = super.loadUser(userRequest);

        // ✅ Extract roles/authorities from the token
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        List<String> authoritiesClaim = oidcUser.getAttribute("authorities");
        if (authoritiesClaim != null) {
            authoritiesClaim.forEach(auth -> mappedAuthorities.add(new SimpleGrantedAuthority(auth.toUpperCase())));
        }

        // ✅ Create a new OidcUser with updated authorities
        return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
