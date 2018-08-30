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
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * 
 * @author Toepfer Martin
 *
 */
public class STWX {

  /**
   * comprises entities: Descriptor, Thsys
   */
  public static final String NAMESPACE = "http://zbw.eu/namespaces/zbw-extensions/";

  public static final URI DESCRIPTOR;

  public static final URI THSYS;

  static {
    final ValueFactory f = ValueFactoryImpl.getInstance();

    DESCRIPTOR = f.createURI(NAMESPACE, "Descriptor");
    THSYS = f.createURI(NAMESPACE, "Thsys");
  }
}
