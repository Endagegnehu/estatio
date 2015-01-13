/*
 *  Copyright 2015 Eurocommercial Properties NV
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
package org.estatio.app.interactivemap;

import javax.inject.Inject;

import org.joda.time.Months;

import org.apache.isis.applib.services.clock.ClockService;

import org.estatio.dom.asset.FixedAsset;
import org.estatio.dom.asset.Unit;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.Occupancies;
import org.estatio.dom.lease.Occupancy;

public class InteractiveMapForFixedAssetColorServiceExpiry implements InteractiveMapForFixedAssetColorService {

    @Override
    public Color getColor(FixedAsset item) {
        if (item instanceof Unit) {
            for (Occupancy occupancy : occupanciesRepo.occupancies((Unit) item)) {
                // TODO: Create repo query for occupancy when EST-471 has been
                // addressed
                final Lease lease = occupancy.getLease();
                if (lease.getTenancyEndDate() != null && lease.getTenancyEndDate().isAfter(clockService.now())) {
                    final int months = Months.monthsBetween(clockService.now(), lease.getTenancyEndDate()).getMonths();
                    int l = months > 50 ? 50 : months;
                    return new Color(ColorUtils.HSLtoRGB(250, 50, l+25), "Months" + months);

                }
            }
            return new Color("red", "Available");
        }
        return null;
    }

    @Inject
    Occupancies occupanciesRepo;

    @Inject
    ClockService clockService;

}
