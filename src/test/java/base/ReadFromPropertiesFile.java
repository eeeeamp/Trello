package base;

import java.io.*;
import java.util.*;

public class ReadFromPropertiesFile {

    public static Properties readPropertiesFile(String fileName) throws IOException {

        FileInputStream fis = null;
        Properties prop = null;

        try {

            fis = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(fis);

        } catch (IOException fileNotFoundException) {

            fileNotFoundException.printStackTrace();

        } finally {

            assert fis != null;
            fis.close();
        }

        return prop;
    }
}
