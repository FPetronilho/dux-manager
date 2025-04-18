package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracktainment.duxmanager.controller.AssetRestController;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.exception.AuthenticationFailedException;
import com.tracktainment.duxmanager.exception.ExceptionDto;
import com.tracktainment.duxmanager.exception.RestExceptionHandler;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.mapper.ExceptionMapperEntryPoint;
import com.tracktainment.duxmanager.usecases.asset.CreateAssetUseCase;
import com.tracktainment.duxmanager.usecases.asset.DeleteAssetUseCase;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;
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
import testutil.TestAssetDataUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AssetRestController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        AssetRestApiTest.TestSecurityConfig.class,
        AssetRestController.class,
        RestExceptionHandler.class
})
class AssetRestApiTest {

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
    private CreateAssetUseCase createAssetUseCase;

    @MockBean
    private ListAssetsByCriteriaUseCase listAssetsByCriteriaUseCase;

    @MockBean
    private DeleteAssetUseCase deleteAssetUseCase;

    private final String digitalUserId = UUID.randomUUID().toString();

    private AssetCreate assetCreate;
    private Asset asset;

    @BeforeEach
    void setUp() {
        assetCreate = TestAssetDataUtil.createTestAssetCreate1();
        asset = TestAssetDataUtil.createTestAsset1();
    }

    @Test
    @WithMockUser
    void shouldCreateAssetSuccessfully() throws Exception {
        // Arrange
        CreateAssetUseCase.Output output = CreateAssetUseCase.Output.builder()
                .asset(asset)
                .build();

        when(createAssetUseCase.execute(any(CreateAssetUseCase.Input.class)))
                .thenReturn(output);

        // Act & Assert
        mockMvc.perform(post("/api/v1/assets/digitalUsers/{digitalUserId}", digitalUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(asset.getId()))
                .andExpect(jsonPath("$.externalId").value(asset.getExternalId()))
                .andExpect(jsonPath("$.type").value("book"));

        verify(createAssetUseCase).execute(any(CreateAssetUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldListAssetsByCriteriaSuccessfully() throws Exception {
        // Arrange
        Asset asset1 = TestAssetDataUtil.createTestAsset1();
        Asset asset2 = TestAssetDataUtil.createTestAsset2();
        List<Asset> assets = Arrays.asList(asset1, asset2);

        ListAssetsByCriteriaUseCase.Output output = ListAssetsByCriteriaUseCase.Output.builder()
                .assets(assets)
                .build();

        when(listAssetsByCriteriaUseCase.execute(any(ListAssetsByCriteriaUseCase.Input.class)))
                .thenReturn(output);

        // Act & Assert
        mockMvc.perform(get("/api/v1/assets")
                        .param("digitalUserId", digitalUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(asset1.getId()))
                .andExpect(jsonPath("$[0].type").value("book"))
                .andExpect(jsonPath("$[1].id").value(asset2.getId()))
                .andExpect(jsonPath("$[1].type").value("game"));

        verify(listAssetsByCriteriaUseCase).execute(any(ListAssetsByCriteriaUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldDeleteAssetSuccessfully() throws Exception {
        // Arrange
        doNothing().when(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/assets")
                        .param("digitalUserId", digitalUserId)
                        .param("externalId", asset.getExternalId()))
                .andExpect(status().isNoContent());

        verify(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenAssetDoesNotExist() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException(Asset.class, asset.getExternalId()))
                .when(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/assets")
                        .param("digitalUserId", digitalUserId)
                        .param("externalId", asset.getExternalId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E-002"))
                .andExpect(jsonPath("$.httpStatusCode").value(404))
                .andExpect(jsonPath("$.reason").value("Resource not found."));

        verify(deleteAssetUseCase).execute(any(DeleteAssetUseCase.Input.class));
    }

    @Test
    @WithMockUser
    void shouldReturnUnauthorizedWhenUserNotAuthenticated() throws Exception {
        // Arrange
        doThrow(new AuthenticationFailedException("Authentication Failed: User ID does not match ID from JWT."))
                .when(createAssetUseCase).execute(any(CreateAssetUseCase.Input.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/assets/digitalUsers/{digitalUserId}", digitalUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetCreate)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("E-004"))
                .andExpect(jsonPath("$.httpStatusCode").value(401))
                .andExpect(jsonPath("$.reason").value("Client not authenticated."));

        verify(createAssetUseCase).execute(any(CreateAssetUseCase.Input.class));
    }

    @Test
    void shouldReturnUnauthorizedWithoutAuthentication() throws Exception {
        // Act & Assert - No @WithMockUser annotation
        mockMvc.perform(get("/api/v1/assets")
                        .param("digitalUserId", digitalUserId))
                .andExpect(status().isUnauthorized());

        verify(listAssetsByCriteriaUseCase, never()).execute(any());
    }
}
