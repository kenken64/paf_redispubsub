package sg.edu.nus.iss.server.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class LineItem {

   private int id;
   private String item;
   private int quantity;

   public int getId() { return id; }
   public void setId(int id) { this.id = id; }

   public String getItem() { return item; }
   public void setItem(String item) { this.item = item; }

   public int getQuantity() { return quantity; }
   public void setQuantity(int quantity) { this.quantity = quantity; }

   @Override
   public String toString() {
      return "LineItem [id=" + id + ", item=" + item + ", quantity=" + quantity + "]";
   }

   public JsonObject toJSON(){
        return Json.createObjectBuilder()
                    .add("product", getItem())
                    .add("quantity", getQuantity())
                    .build();
   }

   public static LineItem createJson(JsonObject o){
      LineItem li = new LineItem();
      li.setItem(o.getString("product"));
      li.setQuantity(o.getJsonNumber("quantity").intValue());
      return li;
   }
}