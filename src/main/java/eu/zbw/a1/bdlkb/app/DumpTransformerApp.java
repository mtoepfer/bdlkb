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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.openrdf.rio.RDFHandlerException;

import com.google.gson.JsonObject;

/**
 * <ul>
 * <li>Read <b>plain text json dump</b> file line by line</li>
 * <li>parse json record, transform into statements</li>
 * <li>write <b>turtle file</b></li>
 * </ul>
 * 
 * @author Toepfer Martin
 *
 */
public class DumpTransformerApp {

  protected static final Logger log = Logger.getLogger(DumpTransformerApp.class);

  public static void main(String[] args)
          throws FileNotFoundException, IOException, RDFHandlerException {
    Properties props = loadProperties();

    String sPthOut = props.getProperty("output");

    // String sPth = "...";
    // try (Stream<JsonObject> stream = openJsonObjectStream(sPth);
    // OutputStream fo = new FileOutputStream(sPthOut);) {
    // DumpTransformer.process(stream, fo);
    // }

    String sPthZip = props.getProperty("zip");
    String zipSubfile = props.getProperty("zip_subfile");
    try (ZipFile zipFile = new ZipFile(sPthZip);) {
      ZipEntry zipEntry = zipFile.getEntry(zipSubfile);
      try (Stream<JsonObject> stream = openJsonObjectStream(zipFile.getInputStream(zipEntry));
              OutputStream fo = new FileOutputStream(sPthOut);) {
        log.info("DUMP PROCESSING: START");
        DumpTransformer.process(stream, fo);
        log.info("DUMP PROCESSING: END");
      }
    }
  }

  private static Properties loadProperties() throws IOException {
    String propsPath = "/dump_transform.properties";
    Properties props = new Properties();
    try (InputStream is = DumpTransformerApp.class.getResource(propsPath).openStream();) {
      props.load(is);
    }
    return props;
  }

  private static Stream<JsonObject> openJsonObjectStream(String sPth) throws IOException {
    return openJsonObjectStream(new FileInputStream(sPth));
  }

  private static Stream<JsonObject> openJsonObjectStream(InputStream is) throws IOException {
    return new BufferedReader(new InputStreamReader(is)).lines().map(DumpTransformer::lineMapper);
  }

}
