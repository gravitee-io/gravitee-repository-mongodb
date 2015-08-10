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
package io.gravitee.repositories.mongodb.internal.model;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.DBRef;

import io.gravitee.repository.model.Policy;

/**
 * Mongo object model for policy configuration
 * 
 * @author Loic DASSONVILLE (loic.dassonville at gmail.com)
 */
public class PolicyConfigurationMongo {

    /**
     * ID of the policy
     */
	@DBRef
    private Policy policy;

    /**
     * Configuration of the policy in JSON format.
     */
    private String configuration;
   

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyConfigurationMongo policyConfiguration = (PolicyConfigurationMongo) o;
        return Objects.equals(this.policy, policyConfiguration.policy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policy);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PolicyConfiguration{");
        sb.append("policy='").append(policy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}