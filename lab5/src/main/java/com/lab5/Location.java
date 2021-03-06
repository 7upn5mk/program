package com.lab5;

public class Location {
    private Double x; //Cant be null
    private Long y; //Cant be null
    private Double z; //Cant be null
    private String name; //Cant be null
    public Location(Double x, Long y, Double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    public Location() {}
    public void setX (Double x) {
        this.x = x;
    }
    public void setY (Long y) {
        this.y = y;
    }
    public void setZ (Double z) {
        this.z = z;
    }
    public void setName (String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Double getX() {
        return x;
    }
    public Double getZ() {
        return z;
    }
    public Long getY() {
        return y;
    }
}