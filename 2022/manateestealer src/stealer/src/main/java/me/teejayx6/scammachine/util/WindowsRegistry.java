package me.teejayx6.scammachine.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WindowsRegistry {
    /**
     *
     * @param location path in the registry
     * @param key registry key
     * @return registry value or null if not found
     */

    public static final String readRegistry(String location, String key) {
        try {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec("reg query " +
                    '"' + location + "\" /v " + key);

            InputStream is = process.getInputStream();
            StringBuilder sw = new StringBuilder();

            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.append((char) c);
            } catch (IOException e) {
            }

            String output = sw.toString();

            // Output has the following format:
            // \n<Version information>\n\n<key>    <registry type>    <value>\r\n\r\n
            int i = output.indexOf("REG_SZ");
            if (i == -1) {
                return null;
            }

            sw = new StringBuilder();
            i += 6; // skip REG_SZ

            // skip spaces or tabs
            for (; ; ) {
                if (i > output.length())
                    break;
                char c = output.charAt(i);
                if (c != ' ' && c != '\t')
                    break;
                ++i;
            }

            // take everything until end of line
            for (; ; ) {
                if (i > output.length())
                    break;
                char c = output.charAt(i);
                if (c == '\r' || c == '\n')
                    break;
                sw.append(c);
                ++i;
            }

            return sw.toString();
        } catch (Exception e) {
            return null;
        }
    }

    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw= new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) {
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}
