package nus.iss.assess.a1.model;

import java.util.List;
import java.util.Map;

public class PurchaseOrder {

    private String name;
    private String address;
    private String email;
    private Integer navigationId;
    private Map<String, Integer> lineItems;

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getAddress() {return this.address;}
    public void setAddress(String address) {this.address = address;}
    
    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}

    public Integer getNavigationId() {return this.navigationId;}
    public void setNavigationId(Integer navigationId) {this.navigationId = navigationId;}

    public Map<String, Integer> getLineItems() {return this.lineItems;}
    public void setLineItems(Map<String, Integer> lineItems) {this.lineItems = lineItems;}

    public Integer getItem(String item) {return this.lineItems.get(item);}
    public Integer getQuantity(String item) {return this.lineItems.get(item);}




    
}
