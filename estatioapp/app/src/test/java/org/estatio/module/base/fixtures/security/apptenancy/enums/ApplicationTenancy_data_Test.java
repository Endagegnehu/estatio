package org.estatio.module.base.fixtures.security.apptenancy.enums;

import org.junit.Test;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import static org.junit.Assert.assertSame;

public class ApplicationTenancy_data_Test {

    @Test
    public void toDomainClass() throws Exception {
        final Class<ApplicationTenancy> cls = ApplicationTenancy_data.Fr.toDomainClass();

        assertSame(cls, ApplicationTenancy.class);
    }
}