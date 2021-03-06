/**
* zaptain-bdlkb | big digital library knowledge base, performance test
* Copyright (C) 2018 ZBW -- Leibniz Information Centre for Economics
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
*/
package eu.zbw.a1.bdlkg.namespace;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

/**
 * 
 * @author Toepfer Martin
 *
 */
public class CustomNameSpaces {

  /**
   * comprises, for example: http://zbw.eu/stw/descriptor/15798-3
   */
  public static final String STW_PREFIX = "http://zbw.eu/stw/";

  public static URI constructStwDescriptorUri(String stwId) {
    return new URIImpl(STW_PREFIX + "descriptor/" + stwId);
  }

  public static final String LOCAL_RESOURCE_PREFIX = "http://www.zbw.eu/record/rdf#";

  public static URI constructResourceUri(String recordId) {
    return new URIImpl(LOCAL_RESOURCE_PREFIX + recordId);
  }

}
