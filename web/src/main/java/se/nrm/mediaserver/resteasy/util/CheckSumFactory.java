package se.nrm.mediaserver.resteasy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;


/**
 *
 * @author ingimar
 */
public class CheckSumFactory {
    private final static Logger logger = Logger.getLogger(CheckSumFactory.class);

    private CheckSumFactory() {
    }

    public static String createMD5ChecksumFromBytestream(byte[] dataBytes){

        MessageDigest md =null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            logger.info("hashChecksum " + ex);
        }
        StringBuilder sb = new StringBuilder();

        byte[] mdbytes = md.digest(dataBytes);
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Deprecated
    public static String getMD5Checksum(String uploadedFileLocation) {
        StringBuilder sb = new StringBuilder();

        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        File file = new File(uploadedFileLocation);

        try (FileInputStream stream = new FileInputStream(file)) {
            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = stream.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };
            byte[] mdbytes = md.digest();

            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        } catch (NoSuchAlgorithmException | IOException ex) {
            logger.info("hashChecksum " + ex);
        }
        return sb.toString();
    }

}
