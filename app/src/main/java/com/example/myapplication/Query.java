package com.example.myapplication;

public class Query {
    private String Fitem,Place;

    public Query(String fitem, String place) {
        Fitem = fitem;
        Place = place;
    }

    public Query(){

    }

    public String getFitem() {
        return Fitem;
    }

    public void setFitem(String fitem) {
        Fitem = fitem;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}
