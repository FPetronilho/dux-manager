package security;

import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.security.DigitalUserSecurityContext;
import com.tracktainment.duxmanager.security.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {

    @InjectMocks
    private SecurityUtil securityUtil;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    private final String subject = "12345";

    @Test
    void shouldGetDigitalUserSuccessfully() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            securityContextHolder.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getPrincipal())
                    .thenReturn(jwt);

            when(jwt.getSubject())
                    .thenReturn(subject);

            // Act
            DigitalUserSecurityContext result = securityUtil.getDigitalUser();

            // Assert
            assertNotNull(result);
            assertEquals(subject, result.getId());
            assertNull(result.getSubject()); // Our implementation doesn't set subject

            // Verify
            securityContextHolder.verify(SecurityContextHolder::getContext);
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
            verify(jwt).getSubject();
        }
    }

    @Test
    void shouldThrowExceptionWhenNoAuthentication() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            securityContextHolder.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(null);

            // Act & Assert
            AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class,
                    () -> securityUtil.getDigitalUser());

            assertEquals("JWT not found in security context.", exception.getMessage());

            // Verify
            securityContextHolder.verify(SecurityContextHolder::getContext);
            verify(securityContext).getAuthentication();
            verifyNoInteractions(jwt);
        }
    }

    @Test
    void shouldThrowExceptionWhenPrincipalIsNotJwt() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            securityContextHolder.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getPrincipal())
                    .thenReturn("not-a-jwt");

            // Act & Assert
            AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class,
                    () -> securityUtil.getDigitalUser());

            assertEquals("JWT not found in security context.", exception.getMessage());

            // Verify
            securityContextHolder.verify(SecurityContextHolder::getContext);
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
            verifyNoInteractions(jwt);
        }
    }

    @Test
    void shouldHandleJwtWithMultipleFields() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            securityContextHolder.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getPrincipal())
                    .thenReturn(jwt);

            when(jwt.getSubject())
                    .thenReturn(subject);

            // Act
            DigitalUserSecurityContext result = securityUtil.getDigitalUser();

            // Assert
            assertNotNull(result);
            assertEquals(subject, result.getId());

            // Verify
            securityContextHolder.verify(SecurityContextHolder::getContext);
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
            verify(jwt).getSubject();
        }
    }
}
