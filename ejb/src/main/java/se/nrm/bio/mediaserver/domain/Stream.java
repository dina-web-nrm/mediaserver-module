package se.nrm.bio.mediaserver.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * i.e:could add condition on start_time vs. end_time
 * @author ingimar
 */
@MappedSuperclass
public abstract class Stream extends Media {

    @Column(name = "START_TIME") 
    private int startTime;

    @Column(name = "END_TIME")
    private int endTime;

    public Stream() {
    }

    public Stream(String owner) {
        super(owner);
    }

    public Stream(String owner, String visibility, String filename, String mimetype) {
        super(owner, visibility, filename, mimetype);
    }
    
    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

}
