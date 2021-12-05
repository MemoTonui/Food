
package com.linda.food.models;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Example implements Serializable
{
    private List<Business> businesses = null;

    public Example() {
    }


    public Example(List<Business> businesses) {
        super();
        this.businesses = businesses;

    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }


}
