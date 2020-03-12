package software.bigbade.enchantmenttokens.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class FileHelper {
    //Private constructor to hide implicit public one
    private FileHelper() {}

    public static JarFile getJarFile(String path) {
        try {
            return new JarFile(path);
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not load jar at " + path);
        }
        return null;
    }

    public static InputStream getJarStream(JarFile file, String name) {
        if(file == null)
            return null;
        try {
            return file.getInputStream(file.getJarEntry(name));
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Could not load file from jar named " + file.getName());
        }
        return null;
    }
}
