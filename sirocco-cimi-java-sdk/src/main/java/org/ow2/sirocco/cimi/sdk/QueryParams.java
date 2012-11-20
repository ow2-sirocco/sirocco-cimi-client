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
package org.ow2.sirocco.cimi.sdk;

/**
 * Helper class used to specify query parameters when retrieving CIMI resources.
 */
public class QueryParams {

    /**
     * Helper class to build a QueryParams instance.
     */
    public static class Builder {
        private Integer first;

        private Integer last;

        private String filter;

        private String expand;

        private String select;

        /**
         * (1-based) ordinal position of the first entity of the collection to
         * return.
         * 
         * @param first the first
         * @return the builder
         */
        public Builder first(final Integer first) {
            this.first = first;
            return this;
        }

        /**
         * (1-based) ordinal position of the last entity of the collection to
         * return.
         * 
         * @param last the last
         * @return the builder
         */
        public Builder last(final Integer last) {
            this.last = last;
            return this;
        }

        /**
         * Set a filter to reduce the number of entities to return.
         * 
         * @param filter expression as defined in DMTF CIMI 1.0 specification,
         *        section 4.1.6.1
         * @return the builder
         */
        public Builder filter(final String filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Comma-seperated list of reference attributes to be expanded.
         * 
         * @param expand the expand
         * @return the builder
         */
        public Builder expand(final String expand) {
            this.expand = expand;
            return this;
        }

        /**
         * Comma-separated list of attributes to be retrieved.
         * 
         * @param select the select
         * @return the builder
         */
        public Builder select(final String select) {
            this.select = select;
            return this;
        }

        /**
         * Creates a builder from a QueryParams instance.
         * 
         * @param from the QueryParams instance
         * @return a builder initialized with the values of the QueryParams
         *         instance
         */
        public Builder fromQueryParams(final QueryParams from) {
            return this.first(from.first).last(from.last).expand(from.expand).select(from.select).filter(from.filter);
        }

        /**
         * Builds a QueryParams.
         * 
         * @return the query params
         */
        public QueryParams build() {
            QueryParams result = new QueryParams();
            result.first = this.first;
            result.last = this.last;
            result.expand = this.expand;
            result.select = this.select;
            result.filter = this.filter;
            return result;
        }
    }

    private Integer first;

    private Integer last;

    private String filter;

    private String expand;

    private String select;

    private QueryParams() {
    }

    /**
     * To builder.
     * 
     * @return the builder
     */
    public Builder toBuilder() {
        return new Builder().fromQueryParams(this);
    }

    /**
     * Builder.
     * 
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Value of "first" query parameter.
     * 
     * @return the first
     */
    public Integer getFirst() {
        return this.first;
    }

    /**
     * Value of "last" query parameter.
     * 
     * @return the last
     */
    public Integer getLast() {
        return this.last;
    }

    /**
     * List of filter expressions.
     * 
     * @return the filter
     */
    public String getFilter() {
        return this.filter;
    }

    /**
     * Value of "expand" query parameter.
     * 
     * @return the expand
     */
    public String getExpand() {
        return this.expand;
    }

    /**
     * Value of "select" query parameter.
     * 
     * @return the select
     */
    public String getSelect() {
        return this.select;
    }

}
