package org.estatio.module.capex.fixtures.order.enums;

import org.apache.isis.applib.fixturescripts.PersonaWithBuilderScript;
import org.apache.isis.applib.fixturescripts.PersonaWithFinder;
import org.apache.isis.applib.services.registry.ServiceRegistry2;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.incode.module.country.dom.impl.Country;
import org.incode.module.country.dom.impl.CountryRepository;
import org.incode.module.country.fixtures.enums.Country_enum;

import org.estatio.module.countryapptenancy.dom.EstatioApplicationTenancyRepositoryForCountry;
import org.estatio.module.numerator.dom.Numerator;
import org.estatio.module.numerator.dom.NumeratorAtPathRepository;
import org.estatio.module.numerator.fixtures.builders.NumeratorBuilder;
import org.estatio.module.party.fixtures.organisation.enums.Organisation_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor()
@Getter
@Accessors(chain = true)
public enum NumeratorForOrder_enum
        implements PersonaWithBuilderScript<Numerator, NumeratorBuilder>, PersonaWithFinder<Numerator> {

    Ita(Country_enum.ITA, "%04d", "Order number", null),
    ItaScopedToHelloWorldIt(Country_enum.ITA, "%04d", "Order number", Organisation_enum.HelloWorldIt),
    Fra(Country_enum.FRA, "%05d", "Order number", null),
    ;

    private final Country_enum country_d;
    private final String format;
    private final String name;
    private final Organisation_enum organisation_d;

    @Override
    public NumeratorBuilder builder() {
        return new NumeratorBuilder()
                .setPrereq((f, ec) -> f.setCountry(f.objectFor(country_d, ec)))
                .setPrereq((f, ec) -> f.setScopedTo(f.objectFor(organisation_d, ec)))
                .setFormat(format)
                .setName(name);
    }

    @Override
    public Numerator findUsing(final ServiceRegistry2 serviceRegistry) {
        final NumeratorAtPathRepository numeratorAtPathRepository = serviceRegistry
                .lookupService(NumeratorAtPathRepository.class);
        final CountryRepository countryRepository = serviceRegistry
                .lookupService(CountryRepository.class);
        final EstatioApplicationTenancyRepositoryForCountry applicationTenancyRepository = serviceRegistry
                .lookupService(EstatioApplicationTenancyRepositoryForCountry.class);

        final Country country = countryRepository.findCountry(country_d.getRef3());
        final ApplicationTenancy applicationTenancy = applicationTenancyRepository.findOrCreateTenancyFor(country);

        return numeratorAtPathRepository.findNumerator(
                name,
                organisation_d == null
                        ? null
                        : organisation_d.findUsing(serviceRegistry),
                applicationTenancy);
    }
}
