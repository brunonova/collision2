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
package brunonova.collision.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import brunonova.collision.core.Collision;
import brunonova.collision.core.Constants;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.lang3.SystemUtils;


/**
 * Desktop launcher for Collision.
 */
public class DesktopLauncher {
    private static final String TAG = DesktopLauncher.class.getName();

    public static void main(String[] args) {
        // Parse comand-line arguments
        OptionParser parser = new OptionParser();
        parser.acceptsAll(Arrays.asList("l", "locale"), "use a different locale").withRequiredArg();
        parser.acceptsAll(Arrays.asList("h", "help"), "show this help message and exit")
                .forHelp();
        parser.acceptsAll(Arrays.asList("v", "version"), "show program version");
        OptionSet options = parser.parse(args);

        if(options.has("help")) {
            try {
                parser.printHelpOn(System.out);
                System.exit(0);
            } catch(IOException ex) {
                Gdx.app.error(TAG, "error showing help message", ex);
                System.exit(1);
            }
        }

        if(options.has("version")) {
            System.out.println("Collision " + Constants.getVersion());
            System.exit(0);
        }

        if(options.has("locale")) {
            String locale = (String) options.valueOf("locale");
            Locale.setDefault(Locale.forLanguageTag(locale));
        }

        // Configure the game
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Collision";
        config.vSyncEnabled = true;
        config.width = Constants.WINDOW_WIDTH;
        config.height = Constants.WINDOW_HEIGHT;
        config.resizable = true;

        // Set preferences file location according to the OS
        if(SystemUtils.IS_OS_WINDOWS) {
            String appDataDir = System.getenv("APPDATA");
            if(appDataDir != null) {
                config.preferencesDirectory = System.getenv("APPDATA") + "\\collision\\";
                config.preferencesFileType = Files.FileType.Absolute;
            } else {
                config.preferencesDirectory = "AppData\\Roaming\\collision\\";
            }
        } else if(SystemUtils.IS_OS_LINUX) {
            config.preferencesDirectory = ".config/collision";
        } else if(SystemUtils.IS_OS_MAC) {
            config.preferencesDirectory = "Library/Application Support/collision/";
        }

        // Add the icons for the window title bar
        config.addIcon(Constants.RES_PATH + "/icons/icon-128x128.png", Files.FileType.Internal);
        config.addIcon(Constants.RES_PATH + "/icons/icon-32x32.png", Files.FileType.Internal);
        config.addIcon(Constants.RES_PATH + "/icons/icon-16x16.png", Files.FileType.Internal);

        // Start the game
        LwjglApplication app = new LwjglApplication(new Collision(config.width, config.height), config);
	}
}
