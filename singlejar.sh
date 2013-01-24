#!/bin/bash

mvn assembly:single

java -jar target/sshmapreduce-1.0-SNAPSHOT-jar-with-dependencies.jar 29
