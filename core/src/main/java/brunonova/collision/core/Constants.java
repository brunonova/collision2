/*
 * Copyright (C) 2017 Bruno Nova <brunomb.nova@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package brunonova.collision.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import java.io.IOException;
import java.util.Properties;


/**
 * Global constants.
 */
public final class Constants {
    /** Base path of the <i>res</i> directory (without the trailing slash). */
    public static final String RES_PATH = "brunonova/collision/res";
    /** Width of the window. */
    public static final int WINDOW_WIDTH = 600;
    /** Height of the window. */
    public static final int WINDOW_HEIGHT = 600;
    /** The background color of the windows. */
    public static final Color BACKGROUND_COLOR = new Color(0.75f, 0.75f, 0.75f, 1);
    /** The number of enemy balls present when the game starts. */
    public static final int STARTING_NUMBER_OF_ENEMY_BALLS = 3;
    /** Minimum amount of time (seconds) for a new bonus to appear. */
    public static final float NEW_BONUS_MIN_TIME = 3;
    /** Maximum amount of time (seconds) for a new bonus to appear. */
    public static final float NEW_BONUS_MAX_TIME = 10;

    private static final String TAG = Constants.class.getName();
    private static String version;

    /**
     * Returns the version of the game.
     * <p>The version is obtained from the version.properties file generated
     * by Gradle.</p>
     * @return Game version.
     */
    public static String getVersion() {
        if(version == null) {
            // Read version from version.properties file
            Properties prop = new Properties();
            try {
                prop.load(Constants.class.getResourceAsStream(RES_PATH + "/version.properties"));
                version = prop.getProperty("version", "N/A");
            } catch(IOException ex) {
                Gdx.app.error(TAG, "error loading version.properties", ex);
                version = "N/A";
            }
        }
        return version;
    }

    // Prevent this class from being instantiated
    private Constants() {
    }
}
