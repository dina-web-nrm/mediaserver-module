package se.nrm.mserver.util;


import java.io.InputStream;

/**
 *
 * @author ingimar
 */
public interface Writeable {

    public void writeBytesTo(byte[] data, String location);

    public void writeStreamToFS(InputStream iStream, String location);
}
