package com.tracktainment.duxmanager.mapper;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {UUID.class}
)
public interface AssetMapperDataProvider {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Asset toAsset(AssetCreate assetCreate);
}
