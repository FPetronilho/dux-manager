package com.tracktainment.duxmanager.dataprovider;

import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.usecases.asset.ListByCriteriaUseCase;

import java.util.List;

public interface DuxDataProvider {

    DigitalUser createDigitalUser(DigitalUserCreate digitalUserCreate);

    DigitalUser findDigitalUserById(String id);

    void deleteDigitalUser(String id);

    Asset createAsset(String digitalUserId, AssetCreate assetCreate);

    Asset findAssetByExternalId(String digitalUserId, String externalId);

    List<Asset> listAssetsByCriteria(ListByCriteriaUseCase.Input input);

    void deleteAsset(String digitalUserId, String externalId);
}
