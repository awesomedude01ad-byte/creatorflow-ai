#!/bin/sh

#
# Copyright (C) 2015-2024 the original authors.
#
# You may obtain a copy of the License at
#      https://www.gnu.org/licenses/gpl-3.0.html
#

DIRNAME=$(dirname "$0")
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")
MAX_FD="maximum"

die() {
    echo
    echo "$*"
    echo
    exit 1
}

warn() {
    echo "$*"
}

command -v java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found."

# Increase the maximum file descriptors
if [ "$MAX_FD" = "maximum" ]; then
    MAX_FD="maximum"
    warn() { echo "$*"; }
    die() { echo "$*"; exit 1; }
fi

CLASSPATH=$DIRNAME/gradle/wrapper/gradle-wrapper.jar

if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

exec "$JAVACMD" \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
