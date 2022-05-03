package NovClient.Util.HWID;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

    public class HWIDUtil {

        private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String oo() throws Exception {
        String serial = sha1(bytesToHex(generateHWID()));
        return serial;
    }

        public static String ooo() {
            String serial = null;
            try {
                serial = sha2(bytesToHex(generateHWID()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serial;
        }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

        private static String sha1(String text) throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return bytesToHex(sha1hash);
        }

        private static String sha2(String text) throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] sha2hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha2hash = md.digest();
            return bytesToHex(sha2hash);
        }

    public static byte[] generateHWID() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            StringBuilder s = new StringBuilder();
            s.append(System.getProperty("os.name"));
            s.append(System.getProperty("os.arch"));
            s.append(System.getProperty("os.version"));
            s.append(Runtime.getRuntime().availableProcessors());
            s.append(System.getenv("PROCESSOR_IDENTIFIER"));
            s.append(System.getenv("PROCESSOR_ARCHITECTURE"));
            s.append(System.getenv("PROCESSOR_ARCHITEW6432"));
            s.append(System.getenv("NUMBER_OF_PROCESSORS"));
            return hash.digest(s.toString().getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Algorithm wasn't found.", e);
        }
    }


    }
