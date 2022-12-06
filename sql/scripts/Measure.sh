#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p $PGPORT sisla016_DB < $DIR/../src/create_tables.sql
sleep 5

echo "Query time without indexes"
cat <(echo '\timing') $DIR/../src/timeQuery.sql | psql -h localhost -p $PGPORT sisla016_DB | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

psql -h localhost -p $PGPORT sisla016_DB < $DIR/../src/create_indexes.sql

echo "Query time with indexes"
cat <(echo '\timing') $DIR/../src/timeQuery.sql |psql -h localhost -p $PGPORT sisla016_DB | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

