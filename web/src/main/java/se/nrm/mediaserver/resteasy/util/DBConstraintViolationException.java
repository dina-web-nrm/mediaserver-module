package se.nrm.mediaserver.resteasy.util;

/**
 *
 * @author ingimar
 */
public class DBConstraintViolationException extends Exception{

    public DBConstraintViolationException(String message) {
        super(message);
    }
    
}
