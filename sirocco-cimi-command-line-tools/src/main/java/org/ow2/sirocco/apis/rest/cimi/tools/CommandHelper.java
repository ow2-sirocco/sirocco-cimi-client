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
package org.ow2.sirocco.apis.rest.cimi.tools;

import java.util.Map;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.apis.rest.cimi.sdk.Resource;

public class CommandHelper {

    public static Table createResourceListTable(final ResourceListParams listParams, final String... attributes) {
        int numAttr = listParams.isSelected(attributes);
        Table table = new Table(numAttr);
        for (String attribute : attributes) {
            if (listParams.isSelected(attribute)) {
                table.addCell(attribute);
            }
        }
        return table;
    }

    public static Table createResourceShowTable(final Resource<?> resource, final ResourceSelectParam selectParam) {
        Table table = new Table(2);
        table.addCell("Attribute");
        table.addCell("Value");

        if (selectParam.isSelected("id")) {
            table.addCell("id");
            table.addCell(resource.getId());
        }

        if (selectParam.isSelected("name")) {
            table.addCell("name");
            table.addCell(resource.getName());
        }

        if (selectParam.isSelected("description")) {
            table.addCell("description");
            table.addCell(resource.getDescription());
        }

        if (selectParam.isSelected("created")) {
            table.addCell("created");
            if (resource.getCreated() != null) {
                table.addCell(resource.getCreated().toString());
            } else {
                table.addCell("");
            }
        }
        if (selectParam.isSelected("updated")) {
            table.addCell("updated");
            if (resource.getUpdated() != null) {
                table.addCell(resource.getUpdated().toString());
            } else {
                table.addCell("");
            }
        }
        if (selectParam.isSelected("properties")) {
            table.addCell("properties");
            StringBuffer sb = new StringBuffer();
            if (resource.getProperties() != null) {
                for (Map.Entry<String, String> prop : resource.getProperties().entrySet()) {
                    sb.append("(" + prop.getKey() + "," + prop.getValue() + ") ");
                }
            }
            table.addCell(sb.toString());
        }
        return table;
    }

    public static void printResourceCommonAttributes(final Table table, final Resource<?> resource,
        final ResourceSelectParam selectParam) {
        if (selectParam.isSelected("id")) {
            table.addCell(resource.getId());
        }
        if (selectParam.isSelected("name")) {
            if (resource.getName() != null) {
                table.addCell(resource.getName());
            } else {
                table.addCell("");
            }
        }
        if (selectParam.isSelected("description")) {
            if (resource.getDescription() != null) {
                table.addCell(resource.getDescription());
            } else {
                table.addCell("");
            }
        }
        if (selectParam.isSelected("created")) {
            table.addCell(resource.getCreated() != null ? resource.getCreated().toString() : "");
        }
        if (selectParam.isSelected("updated")) {
            if (resource.getUpdated() != null) {
                table.addCell(resource.getUpdated().toString());
            } else {
                table.addCell("");
            }
        }
        if (selectParam.isSelected("properties")) {
            StringBuffer sb = new StringBuffer();
            if (resource.getProperties() != null) {
                for (Map.Entry<String, String> prop : resource.getProperties().entrySet()) {
                    sb.append("(" + prop.getKey() + "," + prop.getValue() + ") ");
                }
            }
            table.addCell(sb.toString());
        }
    }

    public static String printKibibytesValue(final int val) {
        String result;
        if (val < 1024) {
            result = val + " KB";
        }
        if (val < 1024 * 1024) {
            result = String.format("%.0f MB", val / 1024.0f);
        } else {
            result = String.format("%.0f GB", ((float) val) / (1024 * 1024));
        }
        return result;
    }

    public static String printKilobytesValue(final int val) {
        String result;
        if (val < 1000) {
            result = val + " KB";
        }
        if (val < 1000 * 1000) {
            result = String.format("%.0f MB", val / 1000.0f);
        } else {
            result = String.format("%.0f GB", ((float) val) / (1000 * 1000));
        }
        return result;
    }

}
