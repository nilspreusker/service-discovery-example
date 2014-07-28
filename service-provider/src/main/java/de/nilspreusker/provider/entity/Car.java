package de.nilspreusker.provider.entity;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class Car {

    private long id;
    private String type;

    public Car() {
    }

    public Car(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

