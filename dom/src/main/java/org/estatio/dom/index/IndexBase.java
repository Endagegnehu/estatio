package org.estatio.dom.index;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Ordering;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Title;

import org.estatio.dom.EstatioRefDataObject;
import org.estatio.dom.WithStartDate;
import org.estatio.dom.utils.Orderings;

@javax.jdo.annotations.PersistenceCapable
@Immutable
public class IndexBase extends EstatioRefDataObject implements Comparable<IndexBase>, WithStartDate {

    // {{ index
    private Index index;
    
    @Title(sequence = "1", append = ", ")
    @MemberOrder(sequence = "1")
    public Index getIndex() {
        return index;
    }

    public void setIndex(final Index index) {
        this.index = index;
    }

    public void modifyIndex(final Index index) {
        Index currentIndex = getIndex();
        if (index == null || index.equals(currentIndex)) {
            return;
        }
        index.addToIndexBases(this);
    }

    public void clearIndex() {
        Index currentIndex = getIndex();
        if (currentIndex == null) {
            return;
        }
        currentIndex.removeFromIndexBases(this);
    }
    // }}
    
    
    
    // {{ startDate
    @javax.jdo.annotations.Persistent
    private LocalDate startDate;

    @Title(sequence = "2")
    @MemberOrder(sequence = "2")
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }
    // }}


    
    // {{ factor
    @javax.jdo.annotations.Persistent
    @javax.jdo.annotations.Column(scale = 4)
    private BigDecimal factor;

    @Optional
    @MemberOrder(sequence = "4")
    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(final BigDecimal factor) {
        this.factor = factor;
    }

    public String validateFactor(final BigDecimal factor) {
        return (getPreviousBase() == null) ? null : (factor == null || factor.compareTo(BigDecimal.ZERO) == 0) ? "Factor is mandatory when there is a previous base" : null;
    }
    // }}

    
    
    // {{ previousBase
    @javax.jdo.annotations.Persistent(mappedBy = "nextBase")
    private IndexBase previousBase;

    @Optional
    @MemberOrder(sequence = "3")
    public IndexBase getPreviousBase() {
        return previousBase;
    }

    public void setPreviousBase(final IndexBase previousBase) {
        this.previousBase = previousBase;
    }
    
    public void modifyPreviousBase(IndexBase previous) {
        setPreviousBase(previous);
        if (previous != null)
            previous.setNextBase(this);
    }
    // }}

    
    // {{ nextBase
    private IndexBase nextBase;

    @Optional
    @MemberOrder(sequence = "5")
    public IndexBase getNextBase() {
        return nextBase;
    }

    public void setNextBase(final IndexBase nextBase) {
        this.nextBase = nextBase;
    }
    // }}


    
    
    // {{ values
    @javax.jdo.annotations.Persistent(mappedBy = "indexBase")
    private SortedSet<IndexValue> values = new TreeSet<IndexValue>();

    @MemberOrder(sequence = "6")
    public SortedSet<IndexValue> getValues() {
        return values;
    }

    public void setValues(final SortedSet<IndexValue> values) {
        this.values = values;
    }

    public void addToValues(final IndexValue value) {
        if (value == null || getValues().contains(value)) {
            return;
        }
        value.clearIndexBase();
        value.setIndexBase(this);
        getValues().add(value);
    }

    public void removeFromValues(final IndexValue value) {
        // check for no-op
        if (value == null || !getValues().contains(value)) {
            return;
        }
        value.setIndexBase(null);
        getValues().remove(value);
    }
    // }}

    
    // {{ action: factorForDate
    @Named("Get Factor For Date") // avoiding the 'get' prefix
    public BigDecimal factorForDate(@Named("Date") LocalDate date) {
        if (date.isBefore(getStartDate())) {
            return getFactor().multiply(getPreviousBase().factorForDate(date));
        }
        return BigDecimal.ONE;
    }
    // }}

    
    // {{ Comparable impl
    @Override
    public int compareTo(IndexBase other) {
        return ORDERING_BY_INDEX.compound(ORDERING_BY_START_DATE_DESC).compare(this, other);
    }
    
    private final static Ordering<IndexBase> ORDERING_BY_INDEX = new Ordering<IndexBase>() {
        public int compare(IndexBase left, IndexBase right) {
            return Ordering.natural().nullsFirst().compare(left.getIndex(), right.getIndex());
        };
    }.nullsFirst();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final static Ordering<IndexBase> ORDERING_BY_START_DATE_DESC = (Ordering)WithStartDate.ORDERING_BY_START_DATE_DESC;

    // }}

}
