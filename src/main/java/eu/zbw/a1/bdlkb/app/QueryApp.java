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
package eu.zbw.a1.bdlkb.app;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import com.bigdata.rdf.sail.webapp.client.IPreparedTupleQuery;
import com.bigdata.rdf.sail.webapp.client.RemoteRepository;
import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;

/**
 * Perform a query, just for debugging.
 * 
 * The queries can also be entered into the local workbench:
 * 
 * http://localhost:9999/blazegraph/#query
 * 
 * which provides more detailed information about execution time etc. ...
 * 
 * @author Toepfer Martin
 *
 */
public class QueryApp {
  protected static final Logger log = Logger.getLogger(QueryApp.class);

  public static void main(String[] args) throws Exception {
    String queryName = "top_wd.sparql"; // count_by_type count_records count_records_by_date count_properties

    String serviceURL = "http://localhost:9999/blazegraph";
    boolean executor = false;
    final RemoteRepositoryManager repoMngr = new RemoteRepositoryManager(serviceURL, executor);
    String namespace = "kb";
    try {
      RemoteRepository repo = repoMngr.getRepositoryForNamespace(namespace);

      String query = new String(Files.readAllBytes(
              (Paths.get(QueryApp.class.getResource("/SPARQL/" + queryName).toURI()))));
      log.info("QUERY:\n======");
      log.info(query);
      log.info("START QUERY:");
      IPreparedTupleQuery theQ = repo.prepareTupleQuery(query);
      TupleQueryResult qr = theQ.evaluate();
      while (qr.hasNext()) {
        BindingSet row = qr.next();
        System.out.println(row);
      }
      log.info("END QUERY.");
    } finally {
      repoMngr.close();
    }
  }

}
