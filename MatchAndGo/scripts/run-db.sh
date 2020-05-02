#!/bin/bash
cd ..
rm -Rf db
mkdir db
cd db
ls 
java -cp ~/.m2/repository/org/hsqldb/hsqldb/2.5.0/hsqldb-2.5.0.jar org.hsqldb.server.Server -database.0 file:mydb -dbname.0 iwdb
