package com.tracktainment.duxmanager.dataprovider.impl;

import com.mongodb.client.result.DeleteResult;
import com.tracktainment.duxmanager.dataprovider.DigitalUserDataProvider;
import com.tracktainment.duxmanager.document.DigitalUserDocument;
import com.tracktainment.duxmanager.domain.DigitalUser;
import com.tracktainment.duxmanager.dto.DigitalUserCreate;
import com.tracktainment.duxmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.duxmanager.exception.ResourceNotFoundException;
import com.tracktainment.duxmanager.mapper.DigitalUserMapperDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DigitalUserDataProviderNoSql implements DigitalUserDataProvider {

    private final DigitalUserMapperDataProvider mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public DigitalUser create(DigitalUserCreate digitalUserCreate) {
        if (existsBySubjectAndIdentityProviderAndTenant(
                digitalUserCreate.getIdentityProviderInformation().getSubject(),
                digitalUserCreate.getIdentityProviderInformation().getIdentityProvider(),
                digitalUserCreate.getIdentityProviderInformation().getTenantId()
        )) {
            throw new ResourceAlreadyExistsException(
                    DigitalUserDocument.class,
                    String.format(
                            "subject: %s, idP: %s, tenantId: %s",
                            digitalUserCreate.getIdentityProviderInformation().getSubject(),
                            digitalUserCreate.getIdentityProviderInformation().getIdentityProvider(),
                            digitalUserCreate.getIdentityProviderInformation().getTenantId()
                    )
            );
        }

        DigitalUserDocument digitalUserDocument = mapper.toDigitalUserDocument(digitalUserCreate);
        digitalUserDocument = mongoTemplate.save(digitalUserDocument);
        return mapper.toDigitalUser(digitalUserDocument);
    }

    @Override
    public DigitalUser findById(String id) {
        return mapper.toDigitalUser(findDigitalUserDocumentById(id));
    }

    @Override
    public DigitalUser findBySubAndIdPAndTenant(String subject, DigitalUser.IdentityProviderInformation.IdentityProvider identityProvider, String tenantId) {
        Query query = new Query().addCriteria(Criteria.where("identityProviderInformation.subject").is(subject))
                .addCriteria(Criteria.where("identityProviderInformation.identityProvider").is(identityProvider))
                .addCriteria(Criteria.where("identityProviderInformation.tenantId").is(tenantId));

        DigitalUserDocument digitalUserDocument = mongoTemplate.findOne(query, DigitalUserDocument.class);
        if (digitalUserDocument == null) {
            throw new ResourceNotFoundException(
                    DigitalUserDocument.class,
                    String.format(
                            "with combination of subject: %s, identity provider: %s and tenant ID: %s",
                            subject,
                            identityProvider,
                            tenantId
                    )
            );
        }

        return mapper.toDigitalUser(digitalUserDocument);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        DeleteResult deleteResult = mongoTemplate.remove(query, DigitalUserDocument.class);

        if (deleteResult.getDeletedCount() == 0) {
            throw new ResourceNotFoundException(DigitalUserDocument.class, id);
        }
    }

    private boolean existsBySubjectAndIdentityProviderAndTenant(
            String subject,
            DigitalUserCreate.IdentityProviderInformation.IdentityProvider identityProvider,
            String tenantId
    ) {
        Query query = new Query()
                .addCriteria(Criteria.where("identityProviderInformation.subject").is(subject))
                .addCriteria(Criteria.where("identityProviderInformation.identityProvider").is(identityProvider))
                .addCriteria(Criteria.where("identityProvider.tenantId").is(tenantId));

        return mongoTemplate.exists(query, DigitalUserDocument.class);
    }

    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, DigitalUserDocument.class);
    }

    public DigitalUserDocument findDigitalUserDocumentById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        DigitalUserDocument digitalUserDocument = mongoTemplate.findOne(query, DigitalUserDocument.class);

        if (digitalUserDocument == null) {
            throw new ResourceNotFoundException(DigitalUserDocument.class, id);
        }

        return digitalUserDocument;
    }
}
