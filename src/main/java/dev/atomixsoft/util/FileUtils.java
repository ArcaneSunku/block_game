package dev.atomixsoft.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileUtils {
    private static final Logger LOG = LogManager.getLogger();

    public static String Read_String_From_Memory(String path) {
        String result;
        try(InputStream is = FileUtils.class.getResourceAsStream(path)) {
            if(is == null) throw new IOException("Failed to read file!");

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while((line = br.readLine()) != null)
                sb.append(line).append("\n");

            br.close();
            result = sb.toString();
        } catch (IOException e) {
            LOG.error("Failed to read file: {}", path);
            throw new RuntimeException();
        }

        return result;
    }

    public static String Read_String(String path) {
        String result;
        try {
            File file = new File(path);
            if(!file.exists()) throw new IOException("Failed to read file!");

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null)
                sb.append(line).append("\n");

            br.close();
            result = sb.toString();
        } catch (IOException e) {
            LOG.error("Failed to read file: {}", path);
            throw new RuntimeException();
        }

        return result;
    }

}
