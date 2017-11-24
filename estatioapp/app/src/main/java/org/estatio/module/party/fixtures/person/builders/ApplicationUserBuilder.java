/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.module.party.fixtures.person.builders;

import javax.inject.Inject;

import org.apache.isis.applib.value.Password;

import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;

import org.estatio.module.party.dom.Person;
import org.estatio.module.party.fixtures.PersonAbstract;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class ApplicationUserBuilder extends PersonAbstract {

    @Getter @Setter
    private String securityUsername;

    @Getter @Setter
    private String securityUserAccountCloneFrom;

    @Getter @Setter
    private Person person;

    @Getter
    ApplicationUser applicationUser;

    @Override
    public void execute(ExecutionContext executionContext) {

        defaultAndCheckParams(executionContext);

        if(securityUsername != null) {
            ApplicationUser userToCloneFrom = applicationUserRepository.findByUsername(securityUserAccountCloneFrom);
            if(userToCloneFrom == null) {
                throw new IllegalArgumentException("Could not find any user with username: " + securityUserAccountCloneFrom);
            }

            applicationUser = applicationUserRepository.newLocalUserBasedOn(
                    securityUsername,
                    new Password("pass"), new Password("pass"),
                    userToCloneFrom, true, null);
            applicationUser.setAtPath(person.getAtPath());
            person.setUsername(securityUsername);

            executionContext.addResult(this, securityUsername, userToCloneFrom);
        }
    }

    public ApplicationUserBuilder defaultAndCheckParams(
            final ExecutionContext executionContext) {

        checkParam("person", executionContext, Person.class);
        checkParam("securityUsername", executionContext, String.class);

        defaultParam("securityUserAccountCloneFrom", executionContext, "estatio-admin");

        return this;
    }

    @Inject
    ApplicationUserRepository applicationUserRepository;

}

