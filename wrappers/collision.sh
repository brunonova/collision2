#!/bin/sh
# Wrapper to run collision
case "$SNAP_ARCH" in
	"amd64") ARCH='x86_64-linux-gnu'
	;;
	"i386") ARCH='i386-linux-gnu'
	;;
	*)
		echo "Unsupported architecture"
		exit 1
	;;
esac

# Mesa Libs
export LD_LIBRARY_PATH=$SNAP/usr/lib/$ARCH/mesa:$LD_LIBRARY_PATH
export LD_LIBRARY_PATH=$SNAP/usr/lib/$ARCH/mesa-egl:$LD_LIBRARY_PATH

# Tell libGL where to find the drivers
export LIBGL_DRIVERS_PATH=$SNAP/usr/lib/$ARCH/dri

# Setup environment variables
export JAVA_HOME="$SNAP/usr/lib/jvm/default-java"
export PATH="$SNAP/usr/lib/jvm/default-java/bin:$SNAP/usr/lib/jvm/default-java/jre/bin:$PATH"


# Launch the game
# Ensure Java looks for the Preferences inside $SNAP_USER_DATA.
# Without this, the old $HOME directory is used.
# Also ensure that "SNAP_USER_DATA" is considered as the home directory.
java -Djava.util.prefs.userRoot="$SNAP_USER_DATA" -Duser.home="$SNAP_USER_DATA" \
     -jar "$SNAP/usr/share/collision/collision.jar" "$@"
