# enviroCar maintenance operations

Some tools and operations for maintenance of enviroCar services.

All tools are based on JUnit tests and corresponding maven profiles
(see pom.xml). They share common configuration properties:

* `all.tracks.firstid`: The first track ID to be considered.
* `all.tracks.lastid`: The last track ID to be considered.

## Aggregate tracks to postgis database

`mvn test -Paggregate-all-tracks`

## Push tracks to target URL

`mvn test -Ppush-all-tracks -Dpush.all.tracks.target=http://localhost:4567`
