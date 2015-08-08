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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.gravitee.repositories.mongodb.internal.model.TeamMongo;
import io.gravitee.repositories.mongodb.internal.team.TeamMongoRepository;
import io.gravitee.repositories.mongodb.mapper.GraviteeMapper;
import io.gravitee.repository.api.TeamRepository;
import io.gravitee.repository.model.Team;


@Component
public class TeamRepositoryImpl implements TeamRepository{

	@Autowired
	private TeamMongoRepository internalTeamRepo;

	@Autowired
	private GraviteeMapper mapper;

	@Override
	public Set<Team> findAll(boolean publicOnly) {
		
		List<TeamMongo> teams = null;
		if(publicOnly){
			teams = internalTeamRepo.findByVisibility(false);
		}else{
			teams = internalTeamRepo.findAll();
		}
		Set<Team> res = mapper.collection2set(teams, TeamMongo.class, Team.class);
		return res;
	}

	@Override
	public Optional<Team> findByName(String name) {
		TeamMongo team = internalTeamRepo.findOne(name);
		return Optional.ofNullable(mapper.map(team, Team.class));
	}

	@Override
	public Team create(Team team) {
		
		TeamMongo teamMongo = mapper.map(team, TeamMongo.class);
		TeamMongo teamMongoCreated = internalTeamRepo.insert(teamMongo);
		return mapper.map(teamMongoCreated, Team.class);
	}

	@Override
	public Team update(Team team) {
		
		TeamMongo teamMongo = internalTeamRepo.findOne(team.getName());
		
		//Update 
		teamMongo.setDescription(team.getDescription());
		teamMongo.setEmail(team.getEmail());
		//teamMongo.setName(team.getName());
		teamMongo.setPrivateTeam(team.isPrivate());
		teamMongo.setUpdatedAt(team.getUpdatedAt());
		
		//FIXME can i change team name ? update team references
		
		TeamMongo teamMongoUpdated = internalTeamRepo.save(teamMongo);
		return mapper.map(teamMongoUpdated, Team.class);
	}

	@Override
	public void delete(String name) {
		
		internalTeamRepo.delete(name);
		
	}
	
}
