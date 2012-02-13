/**
 *
 * SIROCCO
 * Copyright (C) 2011 France Telecom
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
 *  $Id: ProjectInfos.java 308 2011-09-07 15:01:29Z chou $
 *
 */

package org.ow2.sirocco.cloudmanager.api.spec;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "providers")
@XmlRootElement(name = "providers")
public class CloudProviderInfos {

    private Collection<CloudProviderInfo> cloudProviderInfos;

    public Collection<CloudProviderInfo> getCloudProviderInfos() {
        return this.cloudProviderInfos;
    }

    public void setCloudProviderInfos(final Collection<CloudProviderInfo> cloudProviderInfos) {
        this.cloudProviderInfos = cloudProviderInfos;
    }
}
