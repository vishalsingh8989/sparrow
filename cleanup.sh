#!/bin/sh
ps -eaf | grep sparrow
ps -eaf | grep  sparrow | grep -v grep | awk '{print $2}' | xargs kill -9
