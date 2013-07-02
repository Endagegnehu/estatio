/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package org.estatio.services.audit;

import java.util.List;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.audit.AuditingService;

import org.estatio.dom.EstatioDomainService;


@Named("Audit")
@Hidden
public class EstatioAuditService extends EstatioDomainService<AuditEntryForEstatio> implements AuditingService {

    public EstatioAuditService() {
        super(EstatioAuditService.class, AuditEntryForEstatio.class);
    }
    
    public List<AuditEntryForEstatio> list() {
        return allInstances();
    }

    @Override
    @Programmatic
    public void audit(String user, long currentTimestampEpoch, String objectType, String identifier, String preValue, String postValue) {
        AuditEntryForEstatio auditEntry = newTransientInstance();
        auditEntry.setTimestampEpoch(currentTimestampEpoch);
        auditEntry.setUser(user);
        auditEntry.setObjectType(objectType);
        auditEntry.setIdentifier(identifier);
        auditEntry.setPreValue(preValue);
        auditEntry.setPostValue(postValue);
        persist(auditEntry);
    }
}
