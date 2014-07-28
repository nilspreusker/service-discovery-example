package de.nilspreusker.discovery.service;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class InstanceDetails {

    private long version;

    public InstanceDetails() {
        this(0);
    }

    public InstanceDetails(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
