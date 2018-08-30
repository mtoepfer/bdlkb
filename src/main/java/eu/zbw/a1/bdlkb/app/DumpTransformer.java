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

import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.DCTERMS;
import org.openrdf.rio.RDFHandlerException;

import com.bigdata.rdf.rio.turtle.BigdataTurtleWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.zbw.a1.bdlkg.namespace.CustomNameSpaces;

/**
 * <ul>
 * <li>Read <b>plain text json </b>lines</li>
 * <li>parse json record, transform into statements</li>
 * </ul>
 * 
 * @author Toepfer Martin
 *
 */
public class DumpTransformer {

  protected static final Logger log = Logger.getLogger(DumpTransformer.class);

  public static final JsonParser parser = new JsonParser();

  public static void process(Stream<JsonObject> stream, OutputStream fo)
          throws RDFHandlerException {
    final ValueFactory f = ValueFactoryImpl.getInstance();
    BigdataTurtleWriter wr = new BigdataTurtleWriter(fo);

    wr.startRDF();
    wr.handleNamespace(DCTERMS.PREFIX, DCTERMS.NAMESPACE);
    wr.handleNamespace("dctype", "http://purl.org/dc/dcmitype/");
    wr.handleNamespace(DC.PREFIX, DC.NAMESPACE);

    for (JsonObject jobj : (Iterable<JsonObject>) stream::iterator) {
      String econbizId = jobj.get("econbiz_id").getAsString();
      Resource res = new URIImpl(CustomNameSpaces.LOCAL_RESOURCE_PREFIX + econbizId);

      // SIMPLE FIELDS
      Statement stmtTitle = extractStatement(res, jobj, "title", DC.TITLE);
      wr.handleStatement(stmtTitle);

      if (jobj.has("date")) {
        Literal litDate = f.createLiteral(jobj.get("date").getAsInt());
        wr.handleStatement(new StatementImpl(res, DC.DATE, litDate));
      }

      // TYPE
      Statement stmtType = null;
      String mType = jobj.get("type").getAsString();
      URI pType = DCTERMS.TYPE;
      Value oCollection = new URIImpl("http://purl.org/dc/dcmitype/Collection");
      Value oText = new URIImpl("http://purl.org/dc/dcmitype/Text");
      if ("article".equals(mType) || "book".equals(mType)) {
        stmtType = new StatementImpl(res, pType, oText);
      } else if ("journal".equals(mType) || "series".equals(mType)) {
        stmtType = new StatementImpl(res, pType, oCollection);
      } else {
        System.out.println(econbizId + ": " + mType + " | " + null);
      }
      if (stmtType != null) {
        wr.handleStatement(stmtType);
      }

      // SUBJECTs
      if (jobj.has("subject_stw")) {
        // System.out.println(jobj);
        Stream<String> subjStream = StreamSupport
                .stream(jobj.get("subject_stw").getAsJsonArray().spliterator(), false)
                .map(x -> (String) ((JsonObject) x).get("stw_id").getAsString());
        for (String subjId : subjStream.collect(Collectors.toList())) {
          wr.handleStatement(new StatementImpl(res, DC.SUBJECT,
                  new URIImpl(CustomNameSpaces.STW_PREFIX + "descriptor/" + subjId)));
        }
      }
      // if (jobj.has("classification_jel")) {
      // String jel = jobj.get("classification_jel").getAsString();
      // wr.handleStatement(new StatementImpl(res, DC.SUBJECT, f.createLiteral(jel)));
      // }
    }
    wr.endRDF();
  }

  public static Statement extractStatement(Resource res, JsonObject jobj, String key,
          String property) {
    return extractStatement(res, jobj, key, new URIImpl(property));
  }

  public static Statement extractStatement(Resource res, JsonObject jobj, String key, URI p) {
    Value o = new LiteralImpl(jobj.get(key).getAsString());
    return new StatementImpl(res, p, o);
  }

  public static JsonObject lineMapper(String line) {
    return parser.parse(line).getAsJsonObject();
  }

}
