package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracktainment.duxmanager.controller.DigitalUserRestController;
import com.tracktainment.duxmanager.converter.StringToIdentityProviderConverter;
import com.tracktainment.duxmanager.converter.WebConfig;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.exception.ExceptionDto;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.exception.RestExceptionHandler;
import com.tracktainment.duxmanager.mapper.ExceptionMapperEntryPoint;
import com.tracktainment.duxmanager.usecases.digitaluser.CreateDigitalUserUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.DeleteDigitalUserUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserByIdUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserBySubAndIdPAndTenantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import testutil.TestDigitalUserDataUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DigitalUserRestController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        DigitalUserRestApiTest.TestSecurityConfig.class,
        DigitalUserRestController.class,
        RestExceptionHandler.class,
        WebConfig.class,
        StringToIdentityProviderConverter.class
})
class DigitalUserRestApiTest {

    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {

        @Bean
        public JwtDecoder jwtDecoder() {
            return mock(JwtDecoder.class);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                    .csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }

        @Bean
        public ExceptionMapperEntryPoint exceptionMapperEntryPoint() {
            return new ExceptionMapperEntryPoint() {
                @Override
                public ExceptionDto toExceptionDto(com.tracktainment.duxmanager.exception.BusinessException e) {
                    return ExceptionDto.builder()
                            .code(e.getCode())
                            .httpStatusCode(e.getHttpStatusCode())
                            .reason(e.getReason())
                            .message(e.getMessage())
                            .build();
                }
            };
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateDigitalUserUseCase createDigitalUserUseCase;

    @MockBean
    private FindDigitalUserByIdUseCase findDigitalUserByIdUseCase;

    @MockBean
    private FindDigitalUserBySubAndIdPAndTenantUseCase findDigitalUserBySubAndIdPAndTenantUseCase;

    @MockBean
    private DeleteDigitalUserUseCase deleteDigitalUserUseCase;

    private DigitalUserCreate digitalUserCreate;
    private DigitalUser digitalUser;

    @BeforeEach
    void setUp() {
        digitalUserCreate = TestDigitalUserDataUtil.createTestDigitalUserCreate();
        digitalUser = TestDigitalUserDataUtil.createTestDigitalUser();
    }

    @Test
    @WithMockUser
    void shouldCreateDigitalUserSuccessfully() throws Exception {
        // Arrange
        CreateDigitalUserUseCase.Output output = CreateDigitalUserUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(createDigitalUserUseCase.execute(any(CreateDigitalUserUseCase.Input.class)))
                .thenReturn(output);

        // Act & Assert
        mockMvc.perform(post("/api/v1/digitalUsers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(digitalUserCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(digitalUser.getId()))
                .andExpect(jsonPath("$.identityProviderInformation.subject").value("auth2_123456"))
                .andExpect(jsonPath("$.personalInformation.fullName").value("John Doe"));

        verify(createDigitalUserUseCase).execute(any(CreateDigitalUserUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldFindDigitalUserByIdSuccessfully() throws Exception {
        // Arrange
        FindDigitalUserByIdUseCase.Output output = FindDigitalUserByIdUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(findDigitalUserByIdUseCase.execute(any(FindDigitalUserByIdUseCase.Input.class)))
                .thenReturn(output);

        // Act & Assert
        mockMvc.perform(get("/api/v1/digitalUsers/{id}", digitalUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(digitalUser.getId()))
                .andExpect(jsonPath("$.identityProviderInformation.subject").value("auth2_123456"))
                .andExpect(jsonPath("$.personalInformation.fullName").value("John Doe"));

        verify(findDigitalUserByIdUseCase).execute(any(FindDigitalUserByIdUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldFindDigitalUserBySubAndIdPAndTenantSuccessfully() throws Exception {
        // Arrange
        FindDigitalUserBySubAndIdPAndTenantUseCase.Output output = FindDigitalUserBySubAndIdPAndTenantUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(findDigitalUserBySubAndIdPAndTenantUseCase.execute(any(FindDigitalUserBySubAndIdPAndTenantUseCase.Input.class)))
                .thenReturn(output);

        // Act & Assert
        mockMvc.perform(get("/api/v1/digitalUsers")
                        .param(
                                "identityProviderInformation.subject",
                                digitalUser.getIdentityProviderInformation().getSubject()
                        )
                        .param(
                                "identityProviderInformation.identityProvider",
                                digitalUser.getIdentityProviderInformation().getIdentityProvider().getValue()
                        )
                        .param(
                                "identityProviderInformation.tenantId",
                                digitalUser.getIdentityProviderInformation().getTenantId()
                        ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(digitalUser.getId()))
                .andExpect(jsonPath("$.identityProviderInformation.subject").value(
                        digitalUser.getIdentityProviderInformation().getSubject()
                ))
                .andExpect(jsonPath("$.personalInformation.fullName").value("John Doe"));

        verify(findDigitalUserBySubAndIdPAndTenantUseCase).execute(any(FindDigitalUserBySubAndIdPAndTenantUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldDeleteDigitalUserSuccessfully() throws Exception {
        // Arrange
        doNothing().when(deleteDigitalUserUseCase).execute(any(DeleteDigitalUserUseCase.Input.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/digitalUsers/{id}", digitalUser.getId()))
                .andExpect(status().isNoContent());

        verify(deleteDigitalUserUseCase).execute(any(DeleteDigitalUserUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenDigitalUserDoesNotExist() throws Exception {
        // Arrange
        when(findDigitalUserByIdUseCase.execute(any(FindDigitalUserByIdUseCase.Input.class)))
                .thenThrow(new ResourceNotFoundException(DigitalUser.class, digitalUser.getId()));

        // Act & Assert
        mockMvc.perform(get("/api/v1/digitalUsers/{id}", digitalUser.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E-002"))
                .andExpect(jsonPath("$.httpStatusCode").value(404))
                .andExpect(jsonPath("$.reason").value("Resource not found."));

        verify(findDigitalUserByIdUseCase).execute(any(FindDigitalUserByIdUseCase.Input.class));
    }

    @Test
    void shouldReturnUnauthorizedWithoutAuthentication() throws Exception {
        // Act & Assert - No @WithMockUser annotation
        mockMvc.perform(get("/api/v1/digitalUsers/{id}", digitalUser.getId()))
                .andExpect(status().isUnauthorized());

        verify(findDigitalUserByIdUseCase, never()).execute(any());
    }
}
