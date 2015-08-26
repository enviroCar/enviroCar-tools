# enviroCar maintenance operations

Some tools and operations for maintenance of enviroCar services.

All tools are based on JUnit tests and corresponding maven profiles
(see pom.xml). They share common configuration properties:

* `all.tracks.firstid`: The first track ID to be considered.
* `all.tracks.lastid`: The last track ID to be considered.

## Aggregate tracks to postgis database

Aggregate all tracks into a postgis database. This operation uses
a spatial aggregation algorithm to combine measurements that have
neighbours in a configurable distance.

`mvn test -Paggregate-all-tracks`

A settings file is used to configure database connection as well as
algorithm details. If not present at `src/test/resources` the
default one is used.

```java
baseURL=https://envirocar.org/api/stable/
pointDistance=0.00018

databaseName=envirocar_aggregation
databasePath=//localhost:5432
username=postgres
password=postgres

aggregated_MeasurementsTableName=aggregated_measurements
original_MeasurementsTableName=original_measurements
measurement_relationsTableName=measurement_relations
aggregatedTracksTableName=aggregated_tracks

```

## Push tracks to target URL

Push all tracks of the API to a given URL endpoint.

`mvn test -Ppush-all-tracks -Dpush.all.tracks.target=http://localhost:4567`
