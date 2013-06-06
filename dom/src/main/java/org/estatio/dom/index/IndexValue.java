package org.estatio.dom.index;

import java.math.BigDecimal;

import com.google.common.collect.Ordering;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Prototype;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;

import org.estatio.dom.EstatioRefDataObject;
import org.estatio.dom.WithStartDate;
import org.estatio.dom.utils.Orderings;

@javax.jdo.annotations.PersistenceCapable
public class IndexValue extends EstatioRefDataObject implements Comparable<IndexValue>, WithStartDate {

    @javax.jdo.annotations.Persistent
    private LocalDate startDate;

    @MemberOrder(sequence = "1")
    @Title(sequence = "2", prepend = ":")
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    private IndexBase indexBase;

    @Hidden(where = Where.PARENTED_TABLES)
    @Title(sequence = "2")
    @MemberOrder(sequence = "2")
    public IndexBase getIndexBase() {
        return indexBase;
    }

    public void setIndexBase(final IndexBase indexBase) {
        this.indexBase = indexBase;
    }

    public void modifyIndexBase(final IndexBase indexBase) {
        IndexBase currentIndexBase = getIndexBase();
        if (indexBase == null || indexBase.equals(currentIndexBase)) {
            return;
        }
        indexBase.addToValues(this);
    }

    public void clearIndexBase() {
        IndexBase currentIndexBase = getIndexBase();
        if (currentIndexBase == null) {
            return;
        }
        currentIndexBase.removeFromValues(this);
    }

    // //////////////////////////////////////

    @javax.jdo.annotations.Column(scale = 4)
    private BigDecimal value;

    @MemberOrder(sequence = "4")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    // //////////////////////////////////////
    
    @Prototype
    public void remove() {
        getContainer().remove(this);
    }

    // //////////////////////////////////////

    @Override
    public int compareTo(IndexValue o) {
        return ORDERING_BY_INDEX_BASE.compound(ORDERING_BY_START_DATE_DESC).compare(this, o);
    }

    public final static Ordering<IndexValue> ORDERING_BY_INDEX_BASE = new Ordering<IndexValue>() {
        public int compare(IndexValue p, IndexValue q) {
            return Ordering.natural().nullsFirst().compare(p.getIndexBase(), q.getIndexBase());
        }
    };

    public final static Ordering<IndexValue> ORDERING_BY_START_DATE_DESC = new Ordering<IndexValue>() {
        public int compare(IndexValue p, IndexValue q) {
            return Ordering.natural().nullsLast().reverse().compare(p.getStartDate(), q.getStartDate());
        }
    };

}