#!/usr/bin/env bash
source ./scripts/env.sh     #access all the environment variables in env.sh
sbt -Dhttp.port="$TASKLIST_PORT" run    #run sbt server on port 7850