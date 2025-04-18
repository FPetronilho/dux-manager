package converter;

import com.tracktainment.duxmanager.converter.StringToIdentityProviderConverter;
import com.tracktainment.duxmanager.converter.WebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @InjectMocks
    private WebConfig webConfig;

    @Mock
    private FormatterRegistry registry;

    @Captor
    private ArgumentCaptor<Converter<?, ?>> converterCaptor;

    @Test
    void shouldRegisterIdentityProviderConverter() {
        // Act
        webConfig.addFormatters(registry);

        // Assert
        verify(registry).addConverter(converterCaptor.capture());
        Converter<?, ?> registeredConverter = converterCaptor.getValue();
        assertTrue(registeredConverter instanceof StringToIdentityProviderConverter,
                "Expected StringToIdentityProviderConverter to be registered");
    }
}
