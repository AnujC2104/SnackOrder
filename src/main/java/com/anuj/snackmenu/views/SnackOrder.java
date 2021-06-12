package com.anuj.snackmenu.views;

public class SnackOrder {
    private String name="";
    private String snack="";
    private Integer quantity=1;
    // default values
    // this is the data model

    public SnackOrder(String name, String snack, Integer quantity) {
        this.name = name;
        this.snack = snack;
        this.quantity = quantity;
    }

    public SnackOrder() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnack() {
        return snack;
    }

    public void setSnack(String snack) {
        this.snack = snack;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
