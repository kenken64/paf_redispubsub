package sg.edu.nus.iss.server.models;

import java.util.Date;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import java.util.LinkedList;

public class PurchaseOrder {
   private String orderId;
   private String name;
   private String address;
   private Date createdOn;
   private Date lastUpdate;
	private List<LineItem> lineItems = new LinkedList<>();

   public String getOrderId() { return orderId; }
   public void setOrderId(String orderId) { this.orderId = orderId; }

   public String getName() { return name; }
   public void setName(String name) { this.name = name; }

   public String getAddress() { return address; }

   public void setAddress(String address) { this.address = address; }

   public Date getCreatedOn() { return createdOn; }
   public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

   public Date getLastUpdate() { return lastUpdate; }
   public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

	public List<LineItem> getLineItems() { return lineItems; }
	public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }

   @Override
   public String toString() {
      return "PurchaseOrder [orderId=" + orderId + ", name=" + name 
            + ", address=" + address + ", createdOn="
            + createdOn + ", lastUpdate=" + lastUpdate + "]";
   }

   public JsonObject toJSON(){
       JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        List<JsonObject> lineItems = this.getLineItems()
            .stream()
            .map(t -> t.toJSON())
            .toList();
         
         for(JsonObject x: lineItems)
            arrBuilder.add(x);
        return Json.createObjectBuilder()
                    .add("customer_name", getName())
                    .add("ship_address", getAddress())
                    .add("line_items", arrBuilder)
                    .build();
   }

   public static PurchaseOrder createJson(JsonObject o){
      PurchaseOrder po = new PurchaseOrder();
      List<LineItem> result = new LinkedList<LineItem>();
      po.setName(o.getString("customer_name"));
      po.setAddress(o.getString("ship_address"));
      JsonArray liarr = o.getJsonArray("line_items");
      for(int i=0; i < liarr.size(); i++){
         JsonObject x = liarr.getJsonObject(i);
         LineItem t = LineItem.createJson(x);
         result.add(t);
      }
      po.setLineItems(result);
      return po;
   }
}