package com.jadaperkasa.station.service.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerContainer {

    @SerializedName("CustomerObject")
    @Expose
    private List<CustomerObject> customerObject = null;

    public CustomerContainer() { }


    public CustomerContainer(List<CustomerObject> customerObject) {
        this.customerObject = customerObject;
    }

    public List<CustomerObject> getCustomerObject() {
        return customerObject;
    }

    public void setCustomerObject(List<CustomerObject> customerObject) {
        this.customerObject = customerObject;
    }

}
