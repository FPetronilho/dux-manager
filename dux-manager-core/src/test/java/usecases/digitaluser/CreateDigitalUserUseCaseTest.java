package usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.usecases.digitaluser.CreateDigitalUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDigitalUserDataUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDigitalUserUseCaseTest {

    @Mock
    private DigitalUserDataProvider digitalUserDataProvider;

    @InjectMocks
    private CreateDigitalUserUseCase createDigitalUserUseCase;

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
        when(digitalUserDataProvider.create(any(DigitalUserCreate.class)))
                .thenReturn(digitalUser);

        CreateDigitalUserUseCase.Input input = CreateDigitalUserUseCase.Input.builder()
                .digitalUserCreate(digitalUserCreate)
                .build();

        // Act
        CreateDigitalUserUseCase.Output output = createDigitalUserUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getDigitalUser());
        assertEquals(digitalUser.getId(), output.getDigitalUser().getId());
        assertEquals("John Doe", output.getDigitalUser().getPersonalInformation().getFullName());
        assertEquals("auth2|123456", output.getDigitalUser().getIdentityProviderInformation().getSubject());
        assertEquals(1, output.getDigitalUser().getContactMediumList().size());
        assertEquals("john.doe@example.com", output.getDigitalUser().getContactMediumList().get(0)
                .getCharacteristic().getEmailAddress());

        verify(digitalUserDataProvider).create(digitalUserCreate);
    }

    @Test
    void shouldPropagateResourceAlreadyExistsException() {
        // Arrange
        when(digitalUserDataProvider.create(any(DigitalUserCreate.class)))
                .thenThrow(new ResourceAlreadyExistsException(DigitalUser.class, digitalUser.getId()));

        CreateDigitalUserUseCase.Input input = CreateDigitalUserUseCase.Input.builder()
                .digitalUserCreate(digitalUserCreate)
                .build();

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> createDigitalUserUseCase.execute(input));
        verify(digitalUserDataProvider).create(digitalUserCreate);
    }

    @Test
    void shouldPropagateResourceNotFoundException() {
        // Arrange
        when(digitalUserDataProvider.create(any(DigitalUserCreate.class)))
                .thenThrow(new ResourceNotFoundException(DigitalUser.class, digitalUser.getId()));

        CreateDigitalUserUseCase.Input input = CreateDigitalUserUseCase.Input.builder()
                .digitalUserCreate(digitalUserCreate)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> createDigitalUserUseCase.execute(input));
        verify(digitalUserDataProvider).create(digitalUserCreate);
    }
}
