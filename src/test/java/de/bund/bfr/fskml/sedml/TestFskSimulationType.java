/*
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 * Department Biological Safety - BfR
 */
package de.bund.bfr.fskml.sedml;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestFskSimulationType {

    @Test
    public void testConstructor() {
        assertEquals(FskSimulationType.Type.deterministic, new FskSimulationType(FskSimulationType.Type.deterministic).getType());
    }

    @Test
    public void testType() {

        // Build FskSimulationType and check type with getter
        FskSimulationType simType = new FskSimulationType(FskSimulationType.Type.deterministic);
        assertEquals(FskSimulationType.Type.deterministic, simType.getType());

        // Setter should return false if type does not change. Getter should return `deterministic` again.
        assertFalse(simType.setType(FskSimulationType.Type.deterministic));
        assertEquals(FskSimulationType.Type.deterministic, simType.getType());

        // Setter should return true if type changes. Getter should return new type now (`statistic`).
        assertTrue(simType.setType(FskSimulationType.Type.statistic));
        assertEquals(FskSimulationType.Type.statistic, simType.getType());
    }
}
