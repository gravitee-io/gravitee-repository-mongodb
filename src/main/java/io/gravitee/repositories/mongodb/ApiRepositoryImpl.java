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
package io.gravitee.repositories.mongodb;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.gravitee.repositories.mongodb.internal.api.ApiMongoRepository;
import io.gravitee.repositories.mongodb.internal.key.ApiKeyMongoRepository;
import io.gravitee.repositories.mongodb.internal.model.ApiAssociationMongo;
import io.gravitee.repositories.mongodb.internal.model.ApiMongo;
import io.gravitee.repositories.mongodb.internal.model.PolicyConfigurationMongo;
import io.gravitee.repositories.mongodb.internal.model.PolicyMongo;
import io.gravitee.repositories.mongodb.internal.model.UserMongo;
import io.gravitee.repositories.mongodb.internal.policy.PolicyMongoRepository;
import io.gravitee.repositories.mongodb.internal.team.TeamMongoRepository;
import io.gravitee.repositories.mongodb.internal.user.UserMongoRepository;
import io.gravitee.repositories.mongodb.mapper.GraviteeMapper;
import io.gravitee.repository.api.ApiRepository;
import io.gravitee.repository.exceptions.TechnicalException;
import io.gravitee.repository.model.Api;
import io.gravitee.repository.model.OwnerType;
import io.gravitee.repository.model.PolicyConfiguration;

@Component
public class ApiRepositoryImpl implements ApiRepository {
	

	@Autowired
	private ApiKeyMongoRepository internalApiKeyRepo;
	
	@Autowired
	private ApiMongoRepository internalApiRepo;

	@Autowired
	private TeamMongoRepository internalTeamRepo;
	
	@Autowired
	private UserMongoRepository internalUserRepo;
	
	@Autowired
	private PolicyMongoRepository internalPolicyRepo;
	
	@Autowired
	private GraviteeMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(ApiRepositoryImpl.class);

	
	@Override
	public Optional<Api> findByName(String apiName) throws TechnicalException {
		
		ApiMongo apiMongo =  internalApiRepo.findOne(apiName);
		return Optional.ofNullable(mapApi(apiMongo));
	}

	@Override
	public Set<Api> findAll() throws TechnicalException {
		
		List<ApiMongo> apis = internalApiRepo.findAll();
		return mapApis(apis);
	}

	
	@Override
	public Api create(Api api) throws TechnicalException {
		
		ApiMongo apiMongo = mapApi(api);
		ApiMongo apiMongoCreated = internalApiRepo.insert(apiMongo);
		return mapApi(apiMongoCreated);
	}

	@Override
	public Api update(Api api) throws TechnicalException {
		
		ApiMongo apiMongo =	mapApi(api);
		ApiMongo apiMongoUpdated = internalApiRepo.save(apiMongo);
		return mapApi(apiMongoUpdated);
	}

	@Override
	public void delete(String apiName) throws TechnicalException {
		internalApiRepo.delete(apiName);
	}



	@Override
	public Set<Api> findByUser(String username, boolean publicOnly) throws TechnicalException {
		List<ApiMongo> apis = internalApiRepo.findByUser(username, publicOnly);
		return mapApis(apis);
	}


	@Override
	public Set<Api> findByTeam(String teamName,  boolean publicOnly) throws TechnicalException {
	
		List<ApiMongo> apis = internalApiRepo.findByTeam(teamName, publicOnly);
		return mapApis(apis);
	}
	

	@Override
	public Set<Api> findByCreator(String userName) throws TechnicalException {

		List<ApiMongo> apis = internalApiRepo.findByCreator(userName);
		return mapApis(apis);
	}
	
	
	@Override
	public int countByUser(String username,  boolean publicOnly) throws TechnicalException {
		return (int) internalApiRepo.countByUser(username, publicOnly);
	}

	@Override
	public int countByTeam(String teamName,  boolean publicOnly) throws TechnicalException {
		return (int) internalApiRepo.countByTeam(teamName, publicOnly);	
	}
	

	
	private Set<Api> mapApis(Collection<ApiMongo> apis){
	
		Set<Api> res = new HashSet<>();
		for (ApiMongo api : apis) {
			res.add(mapApi(api));
		}
		return res;
	}
	

	private ApiMongo mapApi(Api api){
		
		ApiMongo apiMongo = null;
		if(api != null){
			apiMongo = mapper.map(api, ApiMongo.class);
			
			if(OwnerType.USER.equals(api.getOwnerType())){
				apiMongo.setOwner(internalUserRepo.findOne(api.getOwner()));
			}else{
				apiMongo.setOwner(internalTeamRepo.findOne(api.getOwner()));
			}
			apiMongo.setCreator(internalUserRepo.findOne(api.getCreator()));
		}
		return apiMongo;
	}

	private Api mapApi(ApiMongo apiMongo){
		
		Api api = null;
		if(apiMongo != null){
			api = mapper.map(apiMongo, Api.class);
		
			if(apiMongo.getOwner() != null){
				api.setOwner(apiMongo.getOwner().getName());
				if(apiMongo.getOwner() instanceof UserMongo){
					api.setOwnerType(OwnerType.USER);
				}else{
					api.setOwnerType(OwnerType.TEAM);
				}
			}
			if(apiMongo.getCreator() != null){
				api.setCreator(apiMongo.getCreator().getName());
			}
		}
		return api;
	}
	
	private PolicyConfigurationMongo map(PolicyConfiguration policyConfiguration){
		PolicyConfigurationMongo res = null;
		
		List<PolicyMongo> policiesMongo =  internalPolicyRepo.findByName(policyConfiguration.getPolicy());
		
		if(policiesMongo == null || policiesMongo.isEmpty()){
			throw new IllegalArgumentException(String.format("No policy found with name [%s]", policyConfiguration.getPolicy()));
		}
		
		res = new PolicyConfigurationMongo();
		//FIXME deal with multiple policy version
		res.setPolicy(policiesMongo.get(0));
		res.setConfiguration(policyConfiguration.getConfiguration());
		
		return res;
	}
//	private PolicyConfiguration mapPolicy(PolicyConfigurationMongo apiMongo){
//		PolicyConfiguration res = null;
//		//TODO
//		
//		return res;
//	}
//	

	@Override
	public void updatePoliciesConfiguration(String apiName, List<PolicyConfiguration> policyConfigs) throws TechnicalException {
		
		List<PolicyConfigurationMongo> policiesConfigsMongo = policyConfigs.stream().map(new Function<PolicyConfiguration,PolicyConfigurationMongo>() {

			@Override
			public PolicyConfigurationMongo apply(PolicyConfiguration policy) {
				return map(policy);
			}
		}).collect(Collectors.toList());
		
		this.internalApiRepo.updatePoliciesConfiguration(apiName, policiesConfigsMongo);
		
	}

	@Override
	public void updatePolicyConfiguration(String apiName, PolicyConfiguration policyConfig) throws TechnicalException {
		
		PolicyConfigurationMongo policyConfigMongo = map(policyConfig);
		this.internalApiRepo.updatePolicyConfiguration(apiName, policyConfigMongo);
		
	}

	@Override
	public List<PolicyConfiguration> findPoliciesByApi(String apiName) throws TechnicalException {
		
		 List<PolicyConfigurationMongo>  policies = this.internalApiRepo.findPoliciesByApi(apiName);
		 return  mapper.collection2list(policies, PolicyConfigurationMongo.class, PolicyConfiguration.class);
	}

	@Override
	public Set<Api> findByApplication(String application) throws TechnicalException {
		
		List<ApiAssociationMongo> apiAssociationMongos = internalApiKeyRepo.findByApplication(application);
		
		return apiAssociationMongos.stream().map(new Function<ApiAssociationMongo, Api>() {

			@Override
			public Api apply(ApiAssociationMongo t) {
				return mapper.map(t.getApi(), Api.class);
			}
		}).collect(Collectors.toSet());
		
	}


	
}
