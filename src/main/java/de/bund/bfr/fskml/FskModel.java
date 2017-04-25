/*******************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.fskml;

/**
 * Keeps the paths to the scripts of an FSK model in a COMBINE archive.
 */
public class FskModel {

    /**
     * Model script.
     */
    public String model;

    /**
     * Parameters script.
     */
    public String param;

    /**
     * Visualization script.
     */
    public String viz;

    /**
     * Model meta data.
     */
    public FskMetaData template;

    public FskModel(final String model, final String param,
                    final String viz, final FskMetaData template) {
        this.model = model;
        this.param = param;
        this.viz = viz;
        this.template = template;
    }

    @Override
    public String toString() {
        return template == null ? "" : template.modelId;
    }
}
