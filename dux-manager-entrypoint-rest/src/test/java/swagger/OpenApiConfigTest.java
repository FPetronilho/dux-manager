package swagger;

import com.tracktainment.duxmanager.swagger.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @InjectMocks
    private OpenApiConfig openApiConfig;

    @Test
    void shouldCreateOpenAPIWithCorrectConfig() {
        // Arrange
        String applicationName = "dux-manager";
        String applicationVersion = "0.0.1-SNAPSHOT";

        ReflectionTestUtils.setField(openApiConfig, "applicationName", applicationName);
        ReflectionTestUtils.setField(openApiConfig, "applicationVersion", applicationVersion);

        // Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals(applicationName + " API Documentation", openAPI.getInfo().getTitle());
        assertEquals(applicationVersion, openAPI.getInfo().getVersion());
        assertTrue(openAPI.getInfo().getDescription().contains(applicationName));

        // Check security configuration
        assertNotNull(openAPI.getSecurity());
        assertFalse(openAPI.getSecurity().isEmpty());

        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        assertTrue(securityRequirement.containsKey("bearerAuth"));

        // Check security schemes
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("bearerAuth"));

        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());
    }
}
