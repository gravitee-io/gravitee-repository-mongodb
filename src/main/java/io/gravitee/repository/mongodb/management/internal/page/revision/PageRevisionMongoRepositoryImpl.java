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
package io.gravitee.repository.mongodb.management.internal.page.revision;

import io.gravitee.repository.mongodb.management.internal.model.PageRevisionMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Eric LELEU (eric.leleu at graviteesource.com)
 * @author GraviteeSource Team
 */
public class PageRevisionMongoRepositoryImpl implements PageRevisionMongoRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<PageRevisionMongo> findLastByPageId(String pageId) {
        Query query = new Query();
        query.limit(1);
        query.with(Sort.by(Sort.Direction.DESC, "_id.revision"));
        query.addCriteria(where("_id.pageId").is(pageId));

        PageRevisionMongo revision = mongoTemplate.findOne(query, PageRevisionMongo.class);
        return Optional.ofNullable(revision);
    }
}
