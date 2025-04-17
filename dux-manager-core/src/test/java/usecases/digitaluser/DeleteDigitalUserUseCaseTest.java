package usecases.digitaluser;

import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.usecases.digitaluser.DeleteDigitalUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDigitalUserUseCaseTest {

    @Mock
    private DigitalUserDataProvider digitalUserDataProvider;

    @InjectMocks
    private DeleteDigitalUserUseCase deleteDigitalUserUseCase;

    private final String digitalUserId = UUID.randomUUID().toString();

    @Test
    void shouldDeleteDigitalUserSuccessfully() {
        // Arrange
        doNothing().when(digitalUserDataProvider).delete(digitalUserId);

        DeleteDigitalUserUseCase.Input input = DeleteDigitalUserUseCase.Input.builder()
                .digitalUserId(digitalUserId)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> deleteDigitalUserUseCase.execute(input));
        verify(digitalUserDataProvider).delete(digitalUserId);
    }

    @Test
    void shouldPropagateResourceNotFoundException() {
        // Arrange
        doThrow(new ResourceNotFoundException(DigitalUser.class, digitalUserId))
                .when(digitalUserDataProvider).delete(digitalUserId);

        DeleteDigitalUserUseCase.Input input = DeleteDigitalUserUseCase.Input.builder()
                .digitalUserId(digitalUserId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteDigitalUserUseCase.execute(input));
        verify(digitalUserDataProvider).delete(digitalUserId);
    }
}
