package com.tracktainment.duxmanager.mapper;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-09T23:34:38+0200",
    comments = "version: 1.6.2, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class AssetMapperDataProviderImpl implements AssetMapperDataProvider {

    @Override
    public Asset toAsset(AssetCreate assetCreate) {
        if ( assetCreate == null ) {
            return null;
        }

        Asset.AssetBuilder<?, ?> asset = Asset.builder();

        asset.externalId( assetCreate.getExternalId() );
        asset.type( assetCreate.getType() );
        asset.permissionPolicy( assetCreate.getPermissionPolicy() );
        asset.artifactInformation( assetCreate.getArtifactInformation() );

        asset.id( java.util.UUID.randomUUID().toString() );

        return asset.build();
    }
}
