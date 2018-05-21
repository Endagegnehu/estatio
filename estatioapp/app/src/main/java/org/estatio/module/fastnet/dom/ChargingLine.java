package org.estatio.module.fastnet.dom;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.Indices;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.estatio.module.base.dom.Importable;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "fastnet",
        table = "ChargingLine"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByKeyToLeaseExternalReference", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE keyToLeaseExternalReference == :keyToLeaseExternalReference "),
        @Query(
                name = "findByExportDate", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE exportDate == :exportDate "),
        @Query(
                name = "findByKeyToLeaseExternalReferenceAndExportDate", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE keyToLeaseExternalReference == :keyToLeaseExternalReference && "
                        + "exportDate == :exportDate"),
        @Query(
                name = "findFirstByKeyToLeaseExternalReferenceAndExportDate", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE keyToLeaseExternalReference == :keyToLeaseExternalReference && "
                        + "exportDate == :exportDate "),
        @Query(
                name = "findByKeyToLeaseExternalReferenceAndKeyToChargeReference", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE keyToLeaseExternalReference == :keyToLeaseExternalReference && "
                        + "keyToChargeReference == :keyToChargeReference "
                        + "ORDER BY exportDate DESC"),
        @Query(
                name = "findByKeyToLeaseExternalReferenceAndKeyToChargeReferenceAndFromDat", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE keyToLeaseExternalReference == :keyToLeaseExternalReference && "
                        + "keyToChargeReference == :keyToChargeReference && "
                        + "fromDat == :fromDat "
                        + "ORDER BY exportDate DESC"),
        @Query(
                name = "findUnique", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.fastnet.dom.ChargingLine "
                        + "WHERE keyToLeaseExternalReference == :keyToLeaseExternalReference && "
                        + "keyToChargeReference == :keyToChargeReference && "
                        + "fromDat == :fromDat && "
                        + "tomDat == :tomDat && "
                        + "arsBel == :arsBel && "
                        + "exportDate == :exportDate "),
})
@Indices({
        @Index(name = "ChargingLine_keyToExternalRef_IDX", members = { "keyToLeaseExternalReference" }),
        @Index(name = "ChargingLine_keyToExternalRef_keyToChargeRef_IDX", members = { "keyToLeaseExternalReference", "keyToChargeReference" })
})
@Uniques({
        @Unique(
                name = "ChargingLine_unique_UNQ",
                members = { "keyToLeaseExternalReference", "keyToChargeReference", "fromDat", "tomDat", "arsBel", "exportDate" }
        )
})
@DomainObject(
        editing = Editing.DISABLED,
        objectType = "org.estatio.module.fastnet.dom.ChargingLine"
)
public class ChargingLine implements Importable {

    public ChargingLine() {
    }

    @Getter @Setter
    @Column(allowsNull = "false")
    private String klientKod;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String klientNamn;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String fastighetsNr;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String fastighetsBeteckning;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String objektNr;

    // lease number --> lease#externalReference
    @Getter @Setter
    @Column(allowsNull = "false")
    private String kontraktNr;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String keyToLeaseExternalReference;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String kundNr;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String kod;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "KONT_TEXT")
    private String kontText;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String kod2;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "KONT_TEXT2")
    private String kontText2;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String keyToChargeReference;

    // charge date
    // NOTE: We take the string here because the excel import file we have to handle does not use the date format
    @Getter @Setter
    @Column(allowsNull = "true") // New situation: we found null's in 20-4-2018
    private String fromDat;

    // charge end date
    @Getter @Setter
    @Column(allowsNull = "true")
    private String tomDat;

    @Getter @Setter
    @Column(allowsNull = "false")
    private BigDecimal perBel;

    @Getter @Setter
    @Column(allowsNull = "false")
    // yearly amount (?, amount in swedish = belopp)
    private BigDecimal arsBel;

    // index base date
    @Getter @Setter
    @Column(allowsNull = "true")
    private String basar;

    // invoicing period
    @Getter @Setter
    @Column(allowsNull = "false")
    private String debPer;

    @Getter @Setter
    @Column(allowsNull = "true")
    @PropertyLayout(named = "first_pos_start")
    private String firstPosStart;

    @Getter @Setter
    @Column(allowsNull = "true")
    @PropertyLayout(named = "deb_ny_index")
    private BigDecimal debNyIndex;

    @Getter @Setter
    @Column(allowsNull = "true")
    @PropertyLayout(named = "deb_index_ny_datum")
    private String debIndexNyDatum;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "enhet_andr")
    private int enhetAndr;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "adj_freq_months")
    private int adjFreqMonths;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "adj_delay_months")
    private int adjDelayMonths;

    @Getter @Setter
    @Column(allowsNull = "true")
    @PropertyLayout(named = "deb_index_andel")
    private BigDecimal debIndexAndel;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "bara_hoj")
    private String baraHoj;

    @Getter @Setter
    @Column(allowsNull = "true")
    @PropertyLayout(named = "akt_kpi")
    private BigDecimal aktKpi;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "import_date")
    private LocalDateTime importDate;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(named = "evd-in-sd")
    private LocalDateTime evdInSd;

    @Getter @Setter
    @Column(allowsNull = "false")
    private LocalDate exportDate;

    @Getter @Setter
    @Column(allowsNull = "true")
    private LocalDate applied;

    @Getter @Setter
    @Column(allowsNull = "true")
    private ImportStatus importStatus;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public ImportStatus apply(){
        if (!discardedOrApplied()) {
            ImportStatus result = fastnetImportService.updateOrCreateItemAndTerm(this);
            if (result != null) {
                setApplied(clockService.now());
                setImportStatus(result);
            }
            return result;
        } else {
            return getImportStatus();
        }
    }

    public boolean hideApply(){
        if (discardedOrApplied())
            return true;
        return false;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    public ChargingLine discard(){
        if (!discardedOrApplied()) {
            setImportStatus(ImportStatus.DISCARDED);
        }
        return this;
    }

    public boolean hideDiscard(){
        if (discardedOrApplied())
            return true;
        return false;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    public ChargingLine noUpdate(){
        if (!discardedOrApplied()) {
            setImportStatus(ImportStatus.NO_UPDATE_NEEDED);
            setApplied(clockService.now());
        }
        return this;
    }

    public boolean hideNoUpdate(){
        if (discardedOrApplied())
            return true;
        return false;
    }

    boolean discardedOrApplied() {
        if (getImportStatus()== ImportStatus.DISCARDED || getApplied()!=null){
            return true;
        }
        return false;
    }

    @Override
    public List<Object> importData(final Object previousRow) {
        setKeyToLeaseExternalReference(keyToLeaseExternalReference());
        setKeyToChargeReference(keyToChargeReference());
        setExportDate(getImportDate().toLocalDate());
        if (chargingLineRepository.findUnique(keyToLeaseExternalReference(), keyToChargeReference(), getFromDat(), getTomDat(), getArsBel(), getExportDate()) == null) {
            repositoryService.persistAndFlush(this);
        }
        return Collections.emptyList();
    }

    String keyToLeaseExternalReference() {
        return getKontraktNr() != null ? getKontraktNr().substring(2) : null;
    }

    String keyToChargeReference() {
        return "SE" + getKod() + "-" + getKod2();
    }

    @Inject
    ChargingLineRepository chargingLineRepository;

    @Inject
    RepositoryService repositoryService;

    @Inject
    FastnetImportService fastnetImportService;

    @Inject
    ClockService clockService;

}
