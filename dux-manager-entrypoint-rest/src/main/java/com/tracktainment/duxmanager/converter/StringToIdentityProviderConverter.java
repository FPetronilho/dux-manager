package com.tracktainment.duxmanager.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tracktainment.duxmanager.domain.DigitalUser.IdentityProviderInformation.IdentityProvider;

@Component
public class StringToIdentityProviderConverter implements Converter<String, IdentityProvider> {

    @Override
    public IdentityProvider convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        for (IdentityProvider provider : IdentityProvider.values()) {
            if (provider.getValue().equals(source)) {
                return provider;
            }
        }

        throw new IllegalArgumentException("Invalid identity provider value: " + source);
    }
}
