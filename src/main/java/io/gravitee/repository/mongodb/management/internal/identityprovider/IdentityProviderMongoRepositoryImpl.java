/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.repository.mongodb.management.internal.identityprovider;

import io.gravitee.repository.mongodb.management.internal.model.IdentityProviderMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Florent CHAMFROY (forent.chamfroy at graviteesource.com)
 * @author GraviteeSource Team
 */
public class IdentityProviderMongoRepositoryImpl implements IdentityProviderMongoRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int findMaxIdentityProviderOrganizationIdOrder(String organizationId) {
        Query query = new Query();
        query.limit(1);
        query.with(new Sort(Sort.Direction.DESC, "order"));
        query.addCriteria(where("organizationId").is(organizationId));

        IdentityProviderMongo identityProvider = mongoTemplate.findOne(query, IdentityProviderMongo.class);
        return (identityProvider != null) ? identityProvider.getOrder() : 0;
    }
}