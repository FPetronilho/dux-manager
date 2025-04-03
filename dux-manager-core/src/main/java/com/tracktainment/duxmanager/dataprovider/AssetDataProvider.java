package com.tracktainment.duxmanager.dataprovider;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;

import java.util.List;

public interface AssetDataProvider {

    Asset create(String digitalUserId, AssetCreate assetCreate);

    Asset findByExternalId(String digitalUserId, String externalId);

    List<Asset> listByCriteria(ListAssetsByCriteriaUseCase.Input input);

    void delete(String digitalUserId, String externalId);
}
