#!/bin/sh
mvn compile
mvn package -Dmaven.test.skip=true
