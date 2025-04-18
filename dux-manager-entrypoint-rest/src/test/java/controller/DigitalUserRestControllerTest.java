package controller;

import com.tracktainment.duxmanager.controller.DigitalUserRestController;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.usecases.digitaluser.CreateDigitalUserUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.DeleteDigitalUserUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserByIdUseCase;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserBySubAndIdPAndTenantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import testutil.TestDigitalUserDataUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DigitalUserRestControllerTest {

    @Mock
    private CreateDigitalUserUseCase createDigitalUserUseCase;

    @Mock
    private FindDigitalUserByIdUseCase findDigitalUserByIdUseCase;

    @Mock
    private FindDigitalUserBySubAndIdPAndTenantUseCase findDigitalUserBySubAndIdPAndTenantUseCase;

    @Mock
    private DeleteDigitalUserUseCase deleteDigitalUserUseCase;

    @InjectMocks
    private DigitalUserRestController digitalUserRestController;

    private DigitalUserCreate digitalUserCreate;
    private DigitalUser digitalUser;

    @BeforeEach
    void setUp() {
        digitalUserCreate = TestDigitalUserDataUtil.createTestDigitalUserCreate();
        digitalUser = TestDigitalUserDataUtil.createTestDigitalUser();
    }

    @Test
    void shouldCreateDigitalUserSuccessfully() {
        // Arrange
        CreateDigitalUserUseCase.Output output = CreateDigitalUserUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(createDigitalUserUseCase.execute(any(CreateDigitalUserUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<DigitalUser> response = digitalUserRestController.create(digitalUserCreate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(digitalUser, response.getBody());

        verify(createDigitalUserUseCase).execute(any(CreateDigitalUserUseCase.Input.class));
    }

    @Test
    void shouldFindDigitalUserByIdSuccessfully() {
        // Arrange
        FindDigitalUserByIdUseCase.Output output = FindDigitalUserByIdUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(findDigitalUserByIdUseCase.execute(any(FindDigitalUserByIdUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<DigitalUser> response = digitalUserRestController.findById(digitalUser.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(digitalUser, response.getBody());

        verify(findDigitalUserByIdUseCase).execute(any(FindDigitalUserByIdUseCase.Input.class));
    }

    @Test
    void shouldFindDigitalUserBySubAndIdPAndTenantSuccessfully() {
        // Arrange
        String subject = "auth2|123456";
        DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider =
                DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK;
        String tenantId = "tenant1";

        FindDigitalUserBySubAndIdPAndTenantUseCase.Output output =
                FindDigitalUserBySubAndIdPAndTenantUseCase.Output.builder()
                        .digitalUser(digitalUser)
                        .build();

        when(findDigitalUserBySubAndIdPAndTenantUseCase.execute(any(FindDigitalUserBySubAndIdPAndTenantUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<DigitalUser> response = digitalUserRestController.findBySubAndIdPAndTenant(
                subject, identityProvider, tenantId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(digitalUser, response.getBody());

        verify(findDigitalUserBySubAndIdPAndTenantUseCase).execute(any(FindDigitalUserBySubAndIdPAndTenantUseCase.Input.class));
    }

    @Test
    void shouldDeleteDigitalUserSuccessfully() {
        // Arrange
        doNothing().when(deleteDigitalUserUseCase).execute(any(DeleteDigitalUserUseCase.Input.class));

        // Act
        ResponseEntity<Void> response = digitalUserRestController.delete(digitalUser.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(deleteDigitalUserUseCase).execute(any(DeleteDigitalUserUseCase.Input.class));
    }

    @Test
    void shouldPassCorrectInputToCreateUseCase() {
        // Arrange
        CreateDigitalUserUseCase.Output output = CreateDigitalUserUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(createDigitalUserUseCase.execute(any(CreateDigitalUserUseCase.Input.class)))
                .thenReturn(output);

        // Act
        digitalUserRestController.create(digitalUserCreate);

        // Assert
        verify(createDigitalUserUseCase).execute(argThat(input ->
                input.getDigitalUserCreate().equals(digitalUserCreate)));
    }

    @Test
    void shouldPassCorrectInputToFindByIdUseCase() {
        // Arrange
        FindDigitalUserByIdUseCase.Output output = FindDigitalUserByIdUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(findDigitalUserByIdUseCase.execute(any(FindDigitalUserByIdUseCase.Input.class)))
                .thenReturn(output);

        // Act
        digitalUserRestController.findById(digitalUser.getId());

        // Assert
        verify(findDigitalUserByIdUseCase).execute(argThat(input ->
                input.getDigitalUserId().equals(digitalUser.getId())));
    }

    @Test
    void shouldPassCorrectInputToFindBySubAndIdPAndTenantUseCase() {
        // Arrange
        String subject = "auth2|123456";
        DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider =
                DigitalUser.IdentityProviderInformation.IdentityProvider.KEY_CLOAK;
        String tenantId = "tenant1";

        FindDigitalUserBySubAndIdPAndTenantUseCase.Output output = FindDigitalUserBySubAndIdPAndTenantUseCase.Output.builder()
                .digitalUser(digitalUser)
                .build();

        when(findDigitalUserBySubAndIdPAndTenantUseCase.execute(any(FindDigitalUserBySubAndIdPAndTenantUseCase.Input.class)))
                .thenReturn(output);

        // Act
        digitalUserRestController.findBySubAndIdPAndTenant(subject, identityProvider, tenantId);

        // Assert
        verify(findDigitalUserBySubAndIdPAndTenantUseCase).execute(argThat(input ->
                input.getSubject().equals(subject) &&
                        input.getIdentityProvider().equals(identityProvider) &&
                        input.getTenantId().equals(tenantId)));
    }

    @Test
    void shouldPassCorrectInputToDeleteUseCase() {
        // Arrange
        doNothing().when(deleteDigitalUserUseCase).execute(any(DeleteDigitalUserUseCase.Input.class));

        // Act
        digitalUserRestController.delete(digitalUser.getId());

        // Assert
        verify(deleteDigitalUserUseCase).execute(argThat(input ->
                input.getDigitalUserId().equals(digitalUser.getId())));
    }
}
