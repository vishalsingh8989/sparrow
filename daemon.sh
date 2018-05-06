#!/bin/sh
java -XX:+UseConcMarkSweepGC -cp target/sparrow-1.0-SNAPSHOT.jar edu.berkeley.sparrow.daemon.SparrowDaemon -c deploy/sparrow_configuration.conf &
