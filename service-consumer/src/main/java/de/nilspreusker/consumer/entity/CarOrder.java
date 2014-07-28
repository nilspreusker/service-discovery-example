package de.nilspreusker.consumer.entity;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class CarOrder {

    private long carId;
    private int quantity;

    public CarOrder() {
    }

    public CarOrder(long carId, int quantity) {
        this.carId = carId;
        this.quantity = quantity;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
