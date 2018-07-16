package ml.bmlzootown;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brandon on 2/29/2016.
 */
public class Updater {

    public static String newestVersion;
    public static String newestFile;
    public static String currentVersion;
    public static Plugin plugin = SafeStaff.plugin;

    public static String updatePlugin() {
        File f = new File("plugins/update/SafeStaff.jar");
        if (f.exists()) {
            return "[SafeStaff] Updated file already exists! Restart server to finish update.";
        }
        downloadFile("http://bmlzootown.x10.mx/mc/plugins/safestaff/" + newestFile, "plugins/update/SafeStaff.jar");
        return "[SafeStaff] File downloaded! Restart server to finish update.";
    }

    public static boolean needUpdate() {
        starting();
        if (!currentVersion.equals(newestVersion)) {
            return true;
        }
        return false;
    }

    public static void starting() {
        currentVersion = plugin.getDescription().getVersion();
        try {
            newestVersion = pluginQuery("safestaff","newestversion");
            newestFile = pluginQuery("safestaff","newest");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String pluginQuery(String pl, String get) throws IOException {
        Scanner scanner = new Scanner(new URL("http://bmlzootown.x10.mx/mc/plugins/index.php?p=" + pl + "&q=" + get).openStream(), "UTF-8");
        String page = scanner.useDelimiter("\\A").next();
        scanner.close();
        String out = "false";
        Pattern pattern = Pattern.compile("<p>(.*?)</p>");
        Matcher matcher = pattern.matcher(page);
        while (matcher.find()) {
            out = matcher.group(1);
        }
        return out;
    }

    public static void downloadFile(String link, String directory){
        try {
            URL url = new URL(link);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(directory);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
