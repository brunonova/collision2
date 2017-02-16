package brunonova.collision.core;

import com.badlogic.gdx.Gdx;
import java.io.IOException;
import java.util.Properties;

public final class Constants {
    private static final String TAG = Constants.class.getName();
    private static String version;

    public static String getVersion() {
        if(version == null) {
            // Read version from version.properties file
            Properties prop = new Properties();
            try {
                prop.load(Constants.class.getResourceAsStream("/brunonova/collision/res/version.properties"));
                version = prop.getProperty("version", "N/A");
            } catch(IOException ex) {
                Gdx.app.error(TAG, "error loading version.properties", ex);
                version = "N/A";
            }
        }
        return version;
    }

    private Constants() {
    }
}
