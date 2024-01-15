package sg.edu.nus.iss.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.server.models.PurchaseOrder;

@Repository
public class PurchaseOrderRepository {

   @Autowired
   private JdbcTemplate template;

   public boolean insertPurchaseOrder(PurchaseOrder po) {
      return template.update(Queries.SQL_INSERT_PURCHASE_ORDER
            , po.getOrderId(), po.getName(), po.getAddress()) > 0;
   }
}