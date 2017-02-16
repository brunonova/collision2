package brunonova.collision.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import brunonova.collision.core.Collision;
import brunonova.collision.core.Constants;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import java.io.IOException;
import java.util.Arrays;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class DesktopLauncher {
    private static final String TAG = DesktopLauncher.class.getName();

	public static void main(String[] args) {
        // Parse comand-line arguments
        OptionParser parser = new OptionParser();
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

        // Configure and start the game
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Collision";
        config.vSyncEnabled = true;
        config.width = 600;
        config.height = 600;
        config.resizable = true;
        config.addIcon("brunonova/collision/res/icons/icon-128x128.png", Files.FileType.Internal);
        config.addIcon("brunonova/collision/res/icons/icon-32x32.png", Files.FileType.Internal);
        config.addIcon("brunonova/collision/res/icons/icon-16x16.png", Files.FileType.Internal);
		new LwjglApplication(new Collision(), config);
	}
}
