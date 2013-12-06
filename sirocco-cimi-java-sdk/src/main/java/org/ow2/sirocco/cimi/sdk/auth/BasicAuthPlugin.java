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
package org.ow2.sirocco.cimi.sdk.auth;

import java.util.HashMap;
import java.util.Map;

import org.ow2.sirocco.cimi.sdk.AuthPlugin;
import org.ow2.sirocco.cimi.sdk.CimiClientException;

import com.sun.jersey.core.util.Base64;

/**
 * Plugin implementing HTTP Basic authentication.
 */
public class BasicAuthPlugin implements AuthPlugin {

    /*
     * (non-Javadoc)
     * @see org.ow2.sirocco.cimi.server.sdk.AuthPlugin#authenticate(java.lang.
     * String, java.lang.String)
     */
    @Override
    public Map<String, String> authenticate(final String user, final String password, final String tenantId,
        final String tenantName) throws CimiClientException {
        StringBuilder sbToEncode = new StringBuilder();
        sbToEncode.append(user).append(':').append(password);
        StringBuilder sb = new StringBuilder();
        sb.append("Basic ").append(new String(Base64.encode(sbToEncode.toString())));
        Map<String, String> result = new HashMap<String, String>();
        result.put("Authorization", sb.toString());
        if (tenantId != null) {
            result.put("tenantId", tenantId);
        }
        if (tenantName != null) {
            result.put("tenantName", tenantName);
        }
        return result;
    }

}
