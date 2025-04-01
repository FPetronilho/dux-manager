package com.tracktainment.duxmanager.dataprovider;

import com.mongodb.client.result.DeleteResult;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DigitalUserDataProviderNoSql implements DigitalUserDataProvider {

    private final DigitalUserMapperDataProvider mapper;
    private final MongoTemplate mongoTemplate;

    @Override
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
    public void delete(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
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

    private DigitalUserDocument findDigitalUserDocumentById(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        DigitalUserDocument digitalUserDocument = mongoTemplate.findOne(query, DigitalUserDocument.class);

        digitalUserDocument = Optional.ofNullable(digitalUserDocument).orElseThrow(
                () -> new ResourceNotFoundException(DigitalUserDocument.class, id)
        );

        return digitalUserDocument;
    }
}
