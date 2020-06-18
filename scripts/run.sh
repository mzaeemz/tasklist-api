#!/bin/bash
source ./scripts/env.sh     #access all the environment variables in env.sh

CLEAN="FALSE"
usage()
{
    echo "Usage:"
    echo "args.sh -h "
    echo "args.sh -c "
    echo "args.sh -p <port> "
    echo ""
    echo "   -c         to execute sbt clean before sbt run"
    echo "   -p <port>  to run sbt with http.port=port"
    echo "   -h         help (this output)"
    exit 0
}

while getopts "cp:h" o; do
    case "${o}" in
        c)
            CLEAN="TRUE"
            ;;
        p)
            TASKLIST_PORT=${OPTARG}
            ;;
        h)
            usage
            ;;
    esac
done
shift $((OPTIND-1))


if [[ ${CLEAN} = "TRUE" ]]; then
    sbt clean;
fi
sbt -Dhttp.port="$TASKLIST_PORT" run;    #run sbt server on port 7850
