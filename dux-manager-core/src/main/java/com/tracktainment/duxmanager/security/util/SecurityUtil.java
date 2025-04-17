package com.tracktainment.duxmanager.security.util;

import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.security.context.DigitalUserSecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    public DigitalUserSecurityContext getDigitalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AuthenticationFailedException("JWT not found in security context.");
        }

        DigitalUserSecurityContext digitalUserSecurityContext = new DigitalUserSecurityContext();
        digitalUserSecurityContext.setId(jwt.getSubject());
        return digitalUserSecurityContext;
    }
}
