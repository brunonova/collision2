#!/bin/sh
# Wrapper to run collision

# Setup environment variables
export JAVA_HOME="$SNAP/usr/lib/jvm/default-java"
export PATH="$SNAP/usr/lib/jvm/default-java/bin:$SNAP/usr/lib/jvm/default-java/jre/bin:$PATH"


# Launch the game
# Ensure Java looks for the Preferences inside $SNAP_USER_DATA.
# Without this, the old $HOME directory is used.
# Also ensure that "SNAP_USER_DATA" is considered as the home directory.
java -Djava.util.prefs.userRoot="$SNAP_USER_DATA" -Duser.home="$SNAP_USER_DATA" \
     -jar "$SNAP/usr/share/collision/collision.jar" "$@"
