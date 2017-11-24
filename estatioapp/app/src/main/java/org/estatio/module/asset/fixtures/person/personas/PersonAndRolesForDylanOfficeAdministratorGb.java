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
package org.estatio.module.asset.fixtures.person.personas;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.estatio.module.party.dom.role.PartyRoleTypeEnum;
import org.estatio.module.party.dom.PersonGenderType;
import org.estatio.module.base.fixtures.security.apptenancy.personas.ApplicationTenancyForGb;
import org.estatio.module.asset.fixtures.person.builders.PersonAndCommsAndRelationshipAndFixedAssetRolesBuilder;
import org.estatio.module.party.fixtures.organisation.personas.OrganisationForYoukeaSe;

public class PersonAndRolesForDylanOfficeAdministratorGb extends FixtureScript {

    public static final String REF = "DCLAYTON";
    public static final String SECURITY_USERNAME = REF.toLowerCase();
    public static final String AT_PATH = ApplicationTenancyForGb.PATH;

    @Override
    protected void execute(ExecutionContext executionContext) {

        executionContext.executeChild(this, new OrganisationForYoukeaSe());

        getContainer().injectServicesInto(new PersonAndCommsAndRelationshipAndFixedAssetRolesBuilder())
                    .setAtPath(AT_PATH)
                    .setReference(REF)
                    .setFirstName("Dylan")
                    .setLastName("Clayton")
                    .setPersonGenderType(PersonGenderType.MALE)
                    .addPartyRoleType(PartyRoleTypeEnum.OFFICE_ADMINISTRATOR)
                    .setSecurityUsername(SECURITY_USERNAME)
                .execute(executionContext);
    }

}
