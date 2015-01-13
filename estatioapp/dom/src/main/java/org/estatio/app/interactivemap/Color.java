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

import com.google.common.base.Objects;

public class Color {
    
    final private String color;
    
    final private String label;
    
    public Color(String color, String label) {
        this.color = color;
        this.label = label;
    }

    public String getColor() {
        return color;
    }
    
    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object other) {
        return Objects.equal(this, other);
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }
}
