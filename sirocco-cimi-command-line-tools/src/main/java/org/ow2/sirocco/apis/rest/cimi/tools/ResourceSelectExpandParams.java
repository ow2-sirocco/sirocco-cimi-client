package org.ow2.sirocco.apis.rest.cimi.tools;

import org.ow2.sirocco.apis.rest.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;

public class ResourceSelectExpandParams extends ResourceSelectParam {
    @Parameter(names = "-expand", description = "comma-separated attributes to expand")
    private String expand;

    public String getExpand() {
        return this.expand;
    }

    public ResourceSelectExpandParams(final String... defaultSelectValue) {
        super(defaultSelectValue);
    }

    @Override
    public QueryParams.Builder buildQueryParams() {
        return super.buildQueryParams().expand(this.expand);
    }

}
