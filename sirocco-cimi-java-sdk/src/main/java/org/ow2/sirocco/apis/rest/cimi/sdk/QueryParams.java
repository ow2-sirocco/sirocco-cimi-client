/**
 *
 * SIROCCO
 * Copyright (C) 2012 France Telecom
 * Contact: sirocco@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  $Id$
 *
 */
package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class used to specify query parameters when retrieving CIMI resources
 */
/**
 * @author dangtran
 */
public class QueryParams {
    private int first = -1;

    private int last = -1;

    private List<String> filters = new ArrayList<String>();

    private String expand = null;

    private String select = null;

    private QueryParams() {
    }

    /**
     * Returns new query parameter container
     */
    public static QueryParams build() {
        return new QueryParams();
    }

    /**
     * (1-based) ordinal position of the first entity of the collection to
     * return
     */
    public QueryParams setFirst(final int first) {
        this.first = first;
        return this;
    }

    /**
     * (1-based) ordinal position of the last entity of the collection to return
     */
    public QueryParams setLast(final int last) {
        this.last = last;
        return this;
    }

    /**
     * Adds a filter to reduce the number of entities to return
     * 
     * @param filter expression as defined in DMTF CIMI 1.0 specification,
     *        section 4.1.6.1
     * @return
     */
    public QueryParams addFilter(final String filter) {
        this.filters.add(filter);
        return this;
    }

    /**
     * Comma-seperated list of reference attributes to be expanded
     */
    public QueryParams setExpand(final String expand) {
        this.expand = expand;
        return this;
    }

    /**
     * Value of "first" query parameter
     */
    public int getFirst() {
        return this.first;
    }

    /**
     * Value of "last" query parameter
     */
    public int getLast() {
        return this.last;
    }

    /**
     * List of filter expressions
     */
    public List<String> getFilters() {
        return this.filters;
    }

    /**
     * Value of "expand" query parameter
     */
    public String getExpand() {
        return this.expand;
    }

    /**
     * Value of "select" query parameter
     */
    public String getSelect() {
        return this.select;
    }

    /**
     * Comma-separated list of attributes to be retrieved
     */
    public void setSelect(final String select) {
        this.select = select;
    }

}
