# About

This incubator project comprises code for simple performance testing, intended application:

__bdlkb__ (big digital library knowledge base) is a backend store for various
digital library records as well as multiple thesauri and mappings between them. 
Thus, it enables to semantically access the graph of digital library entities 
and their relations.

A test of applicability has been performed with > 4,400,000 records of the ZBW.

# Usage

Download [blazegraph](https://www.blazegraph.com/).

Set your configuration parameters:
create `dump_transform.properties` (use [dump_transform.properties_template](src/main/resources/dump_transform.properties_template) as template),
and then
transform your dump json data with [DumpTransformerApp](src/main/java/eu/zbw/a1/bdlkb/app/DumpTransformerApp.java).

Prepare input data:
for example,
copy `stw.nt`, `jel.ttl`, `stw_jel_mapping.ttl`, ... to the directory `input_medium`,
and copy the records.ttl you have created to the directory `input_records`.

Then, import data:

```
java -cp blazegraph.jar \\  
    -Xmx4g com.bigdata.rdf.store.DataLoader \\  
    -defaultGraph http://example.org journal.properties input\_medium input\_records
```

Run the server

```
java -server -Xmx4g -jar blazegraph.jar 
```

Then, try some (queries)[src/main/resources/SPARQL], and analyze query speed at
http://localhost:9999/blazegraph/#query .