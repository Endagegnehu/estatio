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
package org.estatio.module.charge.integtests;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.estatio.module.charge.dom.Charge;
import org.estatio.module.charge.dom.ChargeRepository;
import org.estatio.module.charge.fixtures.ChargeRefData;
import org.estatio.module.country.fixtures.enums.Country_data;

public class ChargeRepository_IntegTest extends ChargeModuleIntegTestAbstract {

    public static class FindCharge extends ChargeRepository_IntegTest {

        @Inject
        private ChargeRepository chargeRepository;

        @Test
        public void whenExists() throws Exception {
            // when
            final Charge charge = chargeRepository.findByReference(ChargeRefData.IT_RENT);
            // then
            Assert.assertEquals(charge.getReference(), ChargeRefData.IT_RENT);
        }
    }

    public static class ChargeRepositoryForCountry extends ChargeRepository_IntegTest {

        private List<Charge> gbCharges;


        @Inject
        private ChargeRepository chargeRepository;

        @Before
        public void setUp() throws Exception {

            gbCharges = Lists.newArrayList(
                    chargeRepository.findByReference(ChargeRefData.GB_DISCOUNT),
                    chargeRepository.findByReference(ChargeRefData.GB_SERVICE_CHARGE),
                    chargeRepository.findByReference(ChargeRefData.GB_SERVICE_CHARGE2),
                    chargeRepository.findByReference(ChargeRefData.GB_INCOMING_CHARGE_1),
                    chargeRepository.findByReference(ChargeRefData.GB_INCOMING_CHARGE_2),
                    chargeRepository.findByReference(ChargeRefData.GB_INCOMING_CHARGE_3),
                    chargeRepository.findByReference(ChargeRefData.GB_RENT),
                    chargeRepository.findByReference(ChargeRefData.GB_ENTRY_FEE),
                    chargeRepository.findByReference(ChargeRefData.GB_SERVICE_CHARGE_INDEXABLE),
                    chargeRepository.findByReference(ChargeRefData.GB_TAX),
                    chargeRepository.findByReference(ChargeRefData.GB_TURNOVER_RENT),
                    chargeRepository.findByReference(ChargeRefData.GB_PERCENTAGE),
                    chargeRepository.findByReference(ChargeRefData.GB_DEPOSIT),
                    chargeRepository.findByReference(ChargeRefData.GB_MARKETING));

        }

        @Test
        public void forGlobal() throws Exception {

            // expect
            expectedExceptions.expect(IllegalArgumentException.class);

            // when
            chargeRepository.chargesForCountry("/");
        }

        @Test
        public void forCountry() throws Exception {
            // when
            final List<Charge> chargeList = chargeRepository.chargesForCountry("/" + Country_data.GBR.getRef3());

            // then
            Assertions.assertThat(chargeList).containsOnly(gbCharges.toArray(new Charge[gbCharges.size()]));
        }

        @Test
        public void forProperty() throws Exception {
            // when
            final List<Charge> chargeList = chargeRepository.chargesForCountry("/" + Country_data.GBR.getRef3() + "/OXF");

            // then
            Assertions.assertThat(chargeList).containsOnly(gbCharges.toArray(new Charge[gbCharges.size()]));
        }

        @Test
        public void forLocal() throws Exception {
            // when
            final List<Charge> chargeList = chargeRepository.chargesForCountry("/" + Country_data.GBR.getRef3() + "/OXF/ta");

            // then
            Assertions.assertThat(chargeList).containsOnly(gbCharges.toArray(new Charge[gbCharges.size()]));
        }

    }

}