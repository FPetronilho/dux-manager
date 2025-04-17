package usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.usecases.digitaluser.FindDigitalUserByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDigitalUserDataUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindDigitalUserByIdUseCaseTest {

    @Mock
    private DigitalUserDataProvider digitalUserDataProvider;

    @InjectMocks
    private FindDigitalUserByIdUseCase findDigitalUserByIdUseCase;

    private DigitalUser digitalUser;

    @BeforeEach
    void setUp() {
        digitalUser = TestDigitalUserDataUtil.createTestDigitalUser();
    }

    @Test
    void shouldFindDigitalUserByIdSuccessfully() {
        // Arrange
        when(digitalUserDataProvider.findById(digitalUser.getId()))
                .thenReturn(digitalUser);

        FindDigitalUserByIdUseCase.Input input = FindDigitalUserByIdUseCase.Input.builder()
                .digitalUserId(digitalUser.getId())
                .build();

        // Act
        FindDigitalUserByIdUseCase.Output output = findDigitalUserByIdUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getDigitalUser());
        assertEquals(digitalUser.getId(), output.getDigitalUser().getId());
        assertEquals("John Doe", output.getDigitalUser().getPersonalInformation().getFullName());
        assertEquals("auth2|123456", output.getDigitalUser().getIdentityProviderInformation().getSubject());
        assertEquals(1, output.getDigitalUser().getContactMediumList().size());
        assertEquals("john.doe@example.com", output.getDigitalUser().getContactMediumList().get(0)
                .getCharacteristic().getEmailAddress());

        verify(digitalUserDataProvider).findById(digitalUser.getId());
    }

    @Test
    void shouldPropagateResourceNotFoundException() {
        // Arrange
        when(digitalUserDataProvider.findById(digitalUser.getId()))
                .thenThrow(new ResourceNotFoundException(DigitalUser.class, digitalUser.getId()));

        FindDigitalUserByIdUseCase.Input input = FindDigitalUserByIdUseCase.Input.builder()
                .digitalUserId(digitalUser.getId())
                .build();

        // Act & Assert

        assertThrows(ResourceNotFoundException.class, () -> findDigitalUserByIdUseCase.execute(input));
        verify(digitalUserDataProvider).findById(digitalUser.getId());
    }

    @Test
    void shouldPropagateResourceAlreadyExistsException() {
        // Arrange
        when(digitalUserDataProvider.findById(digitalUser.getId()))
                .thenThrow(new ResourceAlreadyExistsException(DigitalUser.class, digitalUser.getId()));

        FindDigitalUserByIdUseCase.Input input = FindDigitalUserByIdUseCase.Input.builder()
                .digitalUserId(digitalUser.getId())
                .build();

        // Act & Assert

        assertThrows(ResourceAlreadyExistsException.class, () -> findDigitalUserByIdUseCase.execute(input));
        verify(digitalUserDataProvider).findById(digitalUser.getId());
    }
}
