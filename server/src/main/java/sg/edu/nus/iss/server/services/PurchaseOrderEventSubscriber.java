package sg.edu.nus.iss.server.services;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.server.models.PurchaseOrder;
import sg.edu.nus.iss.server.repositories.LineItemRepository;
import sg.edu.nus.iss.server.repositories.PurchaseOrderRepository;


@Service
public class PurchaseOrderEventSubscriber implements MessageListener{
    @Autowired
	private PlatformTransactionManager txMgr;

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private LineItemRepository liRepo;

    @Override
    public void onMessage(Message msg, byte[] pattern) {
        byte[] data = msg.getBody();
        System.out.println(">>>> " + msg.toString());
        // Process data eg if it is JSON
        JsonReader reader = Json.createReader(new ByteArrayInputStream(data));
        JsonObject jsonData = reader.readObject();
        PurchaseOrder po = PurchaseOrder.createJson(jsonData);
        
		TransactionStatus tx = txMgr.getTransaction(null);
        String poId = UUID.randomUUID().toString().substring(0, 8);
        po.setOrderId(poId);
        System.out.println(po.getLineItems().size());
		try {
			boolean result = poRepo.insertPurchaseOrder(po) 
               && liRepo.insertLineItems(poId, po.getLineItems());
			txMgr.commit(tx);
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
    }
    
}
