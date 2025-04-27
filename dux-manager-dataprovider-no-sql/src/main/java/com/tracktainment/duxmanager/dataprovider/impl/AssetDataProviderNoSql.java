package com.tracktainment.duxmanager.dataprovider.impl;

import com.mongodb.BasicDBObject;
import com.tracktainment.duxmanager.dataprovider.AssetDataProvider;
import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.exception.ParameterValidationErrorException;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.mapper.AssetMapperDataProvider;
import com.tracktainment.duxmanager.usecases.asset.ListAssetsByCriteriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetDataProviderNoSql implements AssetDataProvider {

    private final AssetMapperDataProvider mapper;
    private final MongoTemplate mongoTemplate;
    private final DigitalUserDataProviderNoSql digitalUserDataProviderNoSql;

    @Override
    @Transactional
    public Asset create(String digitalUserId, AssetCreate assetCreate) {
        if (existsByExternalId(digitalUserId, assetCreate.getExternalId())) {
            throw new ResourceAlreadyExistsException(Asset.class, assetCreate.getExternalId());
        }

        DigitalUserDocument digitalUserDocument = digitalUserDataProviderNoSql.findDigitalUserDocumentById(digitalUserId);
        Asset asset = mapper.toAsset(assetCreate);
        asset.setCreatedAt(LocalDateTime.now());
        digitalUserDocument.getAssets().add(asset);
        mongoTemplate.save(digitalUserDocument);
        return asset;

    }

    @Override
    public Asset findByExternalId(String digitalUserId, String externalId) {
        Query query = new Query()
                .addCriteria(Criteria.where("id").is(digitalUserId))
                .addCriteria(Criteria.where("assets.externalId").is(externalId));

        query.fields().include("assets.$"); // Projects only the matching asset in a list with 1 element
        DigitalUserDocument digitalUserDocument = mongoTemplate.findOne(query, DigitalUserDocument.class);

        if (digitalUserDocument == null || digitalUserDocument.getAssets().isEmpty()) {
            throw new ResourceNotFoundException(Asset.class, externalId);
        }

        return digitalUserDocument.getAssets().get(0);
    }

    @Override
    public List<Asset> listByCriteria(ListAssetsByCriteriaUseCase.Input input) {
        if (input.getDigitalUserId() == null) {
            throw new ParameterValidationErrorException("digitalUserId cannot be empty");
        }

        String userId = input.getDigitalUserId();
        if (!digitalUserDataProviderNoSql.existsById(userId)) {
            throw new ResourceNotFoundException(DigitalUserDocument.class, userId);
        }

        Query query = new Query(Criteria.where("id").is(userId));
        DigitalUserDocument digitalUserDocument = mongoTemplate.findOne(query, DigitalUserDocument.class);

        if (digitalUserDocument == null || digitalUserDocument.getAssets() == null ||
                digitalUserDocument.getAssets().isEmpty()) {
            return Collections.emptyList();
        }

        return digitalUserDocument.getAssets()
                .stream()
                .filter(asset -> matchesAssetCriteria(asset, input))
                .skip(input.getOffset())
                .limit(input.getLimit())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String digitalUserId, String externalId) {
        if (!existsByExternalId(digitalUserId, externalId)) {
            throw new ResourceNotFoundException(Asset.class, externalId);
        }

        Query query = new Query(Criteria.where("id").is(digitalUserId));
        Update update = new Update().pull("assets", new BasicDBObject("externalId", externalId));
        mongoTemplate.updateFirst(query, update, DigitalUserDocument.class);
    }

    private boolean existsByExternalId(String digitalUserId, String externalId) {
        Query query = new Query().addCriteria(Criteria.where("id").is(digitalUserId))
                .addCriteria(Criteria.where("assets.externalId").is(externalId));

        return mongoTemplate.exists(query, DigitalUserDocument.class);
    }

    private boolean matchesAssetCriteria(Asset asset, ListAssetsByCriteriaUseCase.Input input) {
        if (input.getGroupId() != null) {
            if (asset.getArtifactInformation() == null
                    || !input.getGroupId().equals(asset.getArtifactInformation().getGroupId())) {
                return false;
            }
        }

        if (input.getArtifactId() != null) {
            if (asset.getArtifactInformation() == null
                    || !input.getArtifactId().equals(asset.getArtifactInformation().getArtifactId())) {
                return false;
            }
        }

        if (input.getType() != null && !input.getType().equals(asset.getType())) {
            return false;
        }

        if (input.getExternalIds() != null && !input.getExternalIds().isEmpty()) {
            List<String> externalIds = List.of(input.getExternalIds().split(","));

            if (asset.getExternalId() == null || !externalIds.contains(asset.getExternalId())) {
                return false;
            }
        }

        if (input.getCreatedAt() != null) {
            if (!asset.getCreatedAt().toLocalDate().equals(input.getCreatedAt())) {
                return false;
            }
        } else {
            if (input.getFrom() != null && asset.getCreatedAt().isBefore(input.getFrom().atStartOfDay())) {
                return false;
            }

            if (input.getTo() != null && asset.getCreatedAt()
                    .isAfter(input.getTo().atTime(23, 59, 59, 999999999))) {
                return false;
            }
        }

        return true;
    }
}
