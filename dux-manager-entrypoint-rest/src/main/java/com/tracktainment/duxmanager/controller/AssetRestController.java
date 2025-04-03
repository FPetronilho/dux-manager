package com.tracktainment.duxmanager.controller;

import com.tracktainment.duxmanager.api.AssetRestApi;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.usecases.asset.CreateAssetUseCase;
import com.tracktainment.duxmanager.usecases.asset.DeleteAssetUseCase;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class AssetRestController implements AssetRestApi {

    private final CreateAssetUseCase createAssetUseCase;
    private final ListAssetsByCriteriaUseCase listAssetsByCriteriaUseCase;
    private final DeleteAssetUseCase deleteAssetUseCase;

    @Override
    public ResponseEntity<Asset> create(String digitalUserId, AssetCreate assetCreate) {
        log.info("Creating asset on digital user {}: {}", digitalUserId, assetCreate);
        CreateAssetUseCase.Input input = CreateAssetUseCase.Input.builder()
                .digitalUserId(digitalUserId)
                .assetCreate(assetCreate)
                .build();

        CreateAssetUseCase.Output output = createAssetUseCase.execute(input);
        return new ResponseEntity<>(output.getAsset(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Asset>> listByCriteria(
            Integer offset,
            Integer limit,
            String digitalUserId,
            String externalIds,
            String groupId,
            String artifactId,
            String type,
            LocalDate createdAt,
            LocalDate from,
            LocalDate to
    ) {
        ListAssetsByCriteriaUseCase.Input input = ListAssetsByCriteriaUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .digitalUserId(digitalUserId)
                .externalIds(externalIds)
                .groupId(groupId)
                .artifactId(artifactId)
                .type(type)
                .createdAt(createdAt)
                .from(from)
                .to(to)
                .build();

        log.info("Listing assets by criteria: {}", input);
        ListAssetsByCriteriaUseCase.Output output = listAssetsByCriteriaUseCase.execute(input);
        return new ResponseEntity<>(output.getAssets(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String digitalUserId, String externalId) {
        log.info("Deleting asset with id {} from digital user {}.", externalId, digitalUserId);
        DeleteAssetUseCase.Input input = DeleteAssetUseCase.Input.builder()
                .digitalUserId(digitalUserId)
                .externalId(externalId)
                .build();

        deleteAssetUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
