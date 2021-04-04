FROM ubuntu:latest as init
RUN apt-get -y update
RUN DEBIAN_FRONTEND="noninteractive" apt-get -y install postgis
COPY . .
RUN shp2pgsql -I -s 4326 test.shp Shape > output.sql

FROM kartoza/postgis:latest
COPY --from=init output.sql /docker-entrypoint-initdb.d/
COPY init.sql /docker-entrypoint-initdb.d/