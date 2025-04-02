package com.tracktainment.duxmanager.dataprovider;

import com.mongodb.BasicDBObject;
import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.Asset;
import com.tracktainment.duxmanager.dto.AssetCreate;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.mapper.AssetMapperDataProvider;
import com.tracktainment.duxmanager.usecases.asset.ListByCriteriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Asset> listByCriteria(ListByCriteriaUseCase.Input input) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (input.getDigitalUserId() != null) {
            if (!digitalUserDataProviderNoSql.existsById(input.getDigitalUserId())) {
                throw new ResourceNotFoundException(DigitalUserDocument.class, input.getDigitalUserId());
            } else {
                criteria.and("id").is(input.getDigitalUserId());
            }
        }

        criteria.and("assets.artifactInformation.groupId").is(input.getGroupId())
                .and("assets.artifactInformation.artifactId").is(input.getArtifactId())
                .and("assets.type").is(input.getType());

        if (!input.getExternalIds().isEmpty()) {
            criteria.and("assets.externalId").in(input.getExternalIds());
        }

        if (input.getCreatedAt() != null) {
            criteria.and("assets.createdAt").is(input.getCreatedAt());
        } else {
            if (input.getFrom() != null) {
                criteria.and("assets.createdAt").gte(input.getFrom());
            }
            if (input.getTo() != null) {
                criteria.and("assets.createdAt").lte(input.getTo());
            }
        }

        query.addCriteria(criteria);
        List<DigitalUserDocument> digitalUserDocuments = mongoTemplate.find(query, DigitalUserDocument.class);

        return digitalUserDocuments.stream()
                .flatMap(user -> user.getAssets().stream())
                .skip(input.getOffset())
                .limit(input.getLimit())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String digitalUserId, String externalId) {
        if (existsByExternalId(digitalUserId, externalId)) {
            throw new ResourceAlreadyExistsException(Asset.class, externalId);
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
}
