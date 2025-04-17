package usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserBySubAndIdPAndTenantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.test.TestDigitalUserDataUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindDigitalUserBySubAndIdPAndTenantUseCaseTest {

    @Mock
    private DigitalUserDataProvider digitalUserDataProvider;

    @InjectMocks
    private FindDigitalUserBySubAndIdPAndTenantUseCase findDigitalUserBySubAndIdPAndTenantUseCase;

    private DigitalUser digitalUser;

    @BeforeEach
    void setUp() {
        digitalUser = TestDigitalUserDataUtil.createTestDigitalUser();
    }

    @Test
    void shouldFindDigitalUserBySubAndIdPAndTenantSuccessfully() {
        // Arrange
        when(digitalUserDataProvider.findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        ))
                .thenReturn(digitalUser);

        FindDigitalUserBySubAndIdPAndTenantUseCase.Input input = FindDigitalUserBySubAndIdPAndTenantUseCase.Input.builder()
                .subject(digitalUser.getIdentityProviderInformation().getSubject())
                .identityProvider(digitalUser.getIdentityProviderInformation().getIdentityProvider())
                .tenantId(digitalUser.getIdentityProviderInformation().getTenantId())
                .build();

        // Act
        FindDigitalUserBySubAndIdPAndTenantUseCase.Output output =
                findDigitalUserBySubAndIdPAndTenantUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getDigitalUser());
        assertEquals(digitalUser.getId(), output.getDigitalUser().getId());
        assertEquals("John Doe", output.getDigitalUser().getPersonalInformation().getFullName());
        assertEquals(
                digitalUser.getIdentityProviderInformation().getSubject(),
                output.getDigitalUser().getIdentityProviderInformation().getSubject()
        );
        assertEquals(
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                output.getDigitalUser().getIdentityProviderInformation().getIdentityProvider()
        );
        assertEquals(
                digitalUser.getIdentityProviderInformation().getTenantId(),
                output.getDigitalUser().getIdentityProviderInformation().getTenantId()
        );

        verify(digitalUserDataProvider).findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        );
    }

    @Test
    void shouldPropagateResourceNotFoundException() {
        when(digitalUserDataProvider.findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        ))
                .thenThrow(new ResourceNotFoundException(DigitalUser.class, digitalUser.getId()));

        FindDigitalUserBySubAndIdPAndTenantUseCase.Input input = FindDigitalUserBySubAndIdPAndTenantUseCase.Input.builder()
                .subject(digitalUser.getIdentityProviderInformation().getSubject())
                .identityProvider(digitalUser.getIdentityProviderInformation().getIdentityProvider())
                .tenantId(digitalUser.getIdentityProviderInformation().getTenantId())
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> findDigitalUserBySubAndIdPAndTenantUseCase.execute(input));
        verify(digitalUserDataProvider).findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        );
    }

    @Test
    void shouldPropagateResourceAlreadyExistsException() {
        when(digitalUserDataProvider.findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        ))
                .thenThrow(new ResourceAlreadyExistsException(DigitalUser.class, digitalUser.getId()));

        FindDigitalUserBySubAndIdPAndTenantUseCase.Input input = FindDigitalUserBySubAndIdPAndTenantUseCase.Input.builder()
                .subject(digitalUser.getIdentityProviderInformation().getSubject())
                .identityProvider(digitalUser.getIdentityProviderInformation().getIdentityProvider())
                .tenantId(digitalUser.getIdentityProviderInformation().getTenantId())
                .build();

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> findDigitalUserBySubAndIdPAndTenantUseCase.execute(input));
        verify(digitalUserDataProvider).findBySubAndIdPAndTenant(
                digitalUser.getIdentityProviderInformation().getSubject(),
                digitalUser.getIdentityProviderInformation().getIdentityProvider(),
                digitalUser.getIdentityProviderInformation().getTenantId()
        );
    }
}