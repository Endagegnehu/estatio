/*
 *
 *  Copyright 2012-2015 Eurocommercial Properties NV
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
package org.estatio.dom.budgeting.partioning;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.incode.module.base.dom.utils.TitleBuilder;
import org.incode.module.base.dom.valuetypes.AbstractInterval;
import org.incode.module.base.dom.valuetypes.LocalDateInterval;
import org.incode.module.base.dom.with.WithInterval;

import org.estatio.dom.UdoDomainObject2;
import org.estatio.dom.apptenancy.WithApplicationTenancyProperty;
import org.estatio.dom.asset.Property;
import org.estatio.dom.budgeting.budgetcalculation.BudgetCalculationType;
import org.estatio.dom.charge.Charge;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE
       ,schema = "dbo" // Isis' ObjectSpecId inferred from @DomainObject#objectType
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Queries({
        @Query(
                name = "findUnique", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.partioning.Partitioning " +
                        "WHERE property == :property && type == :type && startDate == :startDate "),
        @Query(
                name = "findByPropertyAndType", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.partioning.Partitioning " +
                        "WHERE property == :property && type == :type ")
})
@Unique(name = "Partitioning_property_type_startDate_UNQ", members = {"property", "type", "startDate"})
@DomainObject(
        objectType = "org.estatio.dom.budgeting.partioning.Partitioning"
)
public class Partitioning extends UdoDomainObject2<Partitioning>
        implements WithApplicationTenancyProperty, WithInterval<Partitioning> {

    public Partitioning() {
        super("property, type, startDate");
    }

    public String title() {

        return TitleBuilder.start()
                .withParent(getProperty())
                .withName(getType())
                .withName(" ")
                .withName(getStartDate())
                .toString();
    }

    @Column(allowsNull = "false", name = "propertyId")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private Property property;

    @Column(allowsNull = "true") //TODO: actually false, but interface WithInterval demands this
    @Getter @Setter
    private LocalDate startDate;

    @Column(allowsNull = "true")  //TODO: actually false, but interface WithInterval demands this
    @Getter @Setter
    private LocalDate endDate;

    @Column(allowsNull = "false")
    @Getter @Setter
    private BudgetCalculationType type;

    @Persistent(mappedBy = "partitioning")
    @Getter @Setter
    private SortedSet<PartitionItem> items = new TreeSet<>();

    @Override
    @PropertyLayout(hidden = Where.EVERYWHERE)
    public ApplicationTenancy getApplicationTenancy() {
        return getProperty().getApplicationTenancy();
    }

    @Programmatic
    public List<Charge> getDistinctInvoiceCharges() {
        List<Charge> results = new ArrayList<>();
        for (PartitionItem item : getItems()){
            if (!results.contains(item.getInvoiceCharge())){
                results.add(item.getInvoiceCharge());
            }
        }
        return results;
    }

    @Programmatic
    public BigDecimal getFractionOfYear(){
        BigDecimal numberOfDaysInInterval = BigDecimal
                .valueOf(getInterval().days());
        BigDecimal numberOfDaysInYear = BigDecimal
                .valueOf(
                        new LocalDateInterval(
                                new LocalDate(getStartDate().getYear(),01,01),
                                new LocalDate(getStartDate().getYear()+1, 01,01), AbstractInterval.IntervalEnding.EXCLUDING_END_DATE)
                                .days()
                );
        return numberOfDaysInInterval.divide(numberOfDaysInYear, MathContext.DECIMAL64);
    }

    @Override public LocalDateInterval getInterval() {
        return LocalDateInterval.including(getStartDate(), getEndDate());
    }

    @Override public LocalDateInterval getEffectiveInterval() {
        return getInterval();
    }

    @Override public boolean isCurrent() {
        return isActiveOn(getClockService().now());
    }

    private boolean isActiveOn(final LocalDate date) {
        return getInterval().contains(date);
    }
}