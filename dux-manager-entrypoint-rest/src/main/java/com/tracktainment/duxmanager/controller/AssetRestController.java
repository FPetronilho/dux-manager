package com.tracktainment.duxmanager.controller;

import com.tracktainment.duxmanager.api.AssetRestApi;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.usecases.asset.CreateUseCase;
import com.tracktainment.duxmanager.usecases.asset.DeleteUseCase;
import com.tracktainment.duxmanager.usecases.asset.ListByCriteriaUseCase;
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

    private final CreateUseCase createUseCase;
    private final ListByCriteriaUseCase listByCriteriaUseCase;
    private final DeleteUseCase deleteUseCase;

    @Override
    public ResponseEntity<Asset> create(String digitalUserId, AssetCreate assetCreate) {
        log.info("Create asset on digital user {}: {}", digitalUserId, assetCreate);
        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .digitalUserId(digitalUserId)
                .assetCreate(assetCreate)
                .build();

        CreateUseCase.Output output = createUseCase.execute(input);
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
        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
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
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);
        return new ResponseEntity<>(output.getAssets(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String digitalUserId, String externalId) {
        log.info("Deleting asset with id {} from digital user {}.", externalId, digitalUserId);
        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .digitalUserId(digitalUserId)
                .externalId(externalId)
                .build();

        deleteUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
