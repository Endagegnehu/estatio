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

package org.estatio.services.settings;

import org.joda.time.LocalDate;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.services.settings.SettingType;

/**
 * Factors out common implementation; however this is NOT annotated with 
 * {@link javax.jdo.annotations.PersistenceCapable}, so that each subclass is its own root entity.
 */
public abstract class SettingAbstractForEstatio 
    extends org.apache.isis.applib.services.settings.SettingAbstract 
    implements org.apache.isis.applib.services.settings.ApplicationSetting {

    // //////////////////////////////////////

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @MemberOrder(name="Description", sequence="1")
    @Named("Update")
    public SettingAbstractForEstatio updateDescription(
            final @Named("Description") @Optional String description) {
        setDescription(description);
        return this;
    }
    public String default0UpdateDescription() {
        return getDescription();
    }
    
    // //////////////////////////////////////

    private SettingType type;

    public SettingType getType() {
        return type;
    }

    public void setType(final SettingType type) {
        this.type = type;
    }

    // //////////////////////////////////////

    private String valueRaw;

    public String getValueRaw() {
        return valueRaw;
    }

    public void setValueRaw(final String valueAsRaw) {
        this.valueRaw = valueAsRaw;
    }

    // //////////////////////////////////////
    
    @MemberOrder(name="ValueAsString", sequence="1")
    @Named("Update")
    public SettingAbstractForEstatio updateAsString(
            final @Named("Value") String value) {
        setValueRaw(value);
        return this;
    }
    public String default0UpdateAsString() {
        return getValueAsString();
    }
    public boolean hideUpdateAsString() {
        return typeIsNot(SettingType.STRING);
    }
    
    @MemberOrder(name="ValueAsInt", sequence="1")
    @Named("Update")
    public SettingAbstractForEstatio updateAsInt(
            final @Named("Value") Integer value) {
        setValueRaw(value.toString());
        return this;
    }
    public Integer default0UpdateAsInt() {
        return getValueAsInt();
    }
    public boolean hideUpdateAsInt() {
        return typeIsNot(SettingType.INT);
    }
    
    @MemberOrder(name="ValueAsLong", sequence="1")
    @Named("Update")
    public SettingAbstractForEstatio updateAsLong(
            final @Named("Value") Long value) {
        setValueRaw(value.toString());
        return this;
    }
    public Long default0UpdateAsLong() {
        return getValueAsLong();
    }
    public boolean hideUpdateAsLong() {
        return typeIsNot(SettingType.LONG);
    }
    
    @MemberOrder(name="ValueAsLocalDate", sequence="1")
    @Named("Update")
    public SettingAbstractForEstatio updateAsLocalDate(
            final @Named("Value") LocalDate value) {
        setValueRaw(value.toString(DATE_FORMATTER));
        return this;
    }
    public LocalDate default0UpdateAsLocalDate() {
        return getValueAsLocalDate();
    }
    public boolean hideUpdateAsLocalDate() {
        return typeIsNot(SettingType.LOCAL_DATE);
    }

    @MemberOrder(name="ValueAsBoolean", sequence="1")
    @Named("Update")
    public SettingAbstractForEstatio updateAsBoolean(
            final @Named("Value") Boolean value) {
        setValueRaw(value.toString());
        return this;
    }
    public Boolean default0UpdateAsBoolean() {
        return getValueAsBoolean();
    }
    public boolean hideUpdateAsBoolean() {
        return typeIsNot(SettingType.BOOLEAN);
    }
    
    // //////////////////////////////////////
    
    
    public SettingAbstractForEstatio delete(
            final @Named("Are you sure?") @Optional Boolean confirm) {
        if(confirm == null || !confirm) {
            container.informUser("Setting NOT deleted");
            return this;
        }
        container.remove(this);
        container.informUser("Setting deleted");
        return null;
    }
    
 
    // //////////////////////////////////////
    
    private DomainObjectContainer container;

    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }


}
