package sg.edu.nus.iss.server.services;

import java.nio.channels.Channel;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.server.models.PurchaseOrder;
import sg.edu.nus.iss.server.repositories.LineItemRepository;
import sg.edu.nus.iss.server.repositories.PurchaseOrderException;
import sg.edu.nus.iss.server.repositories.PurchaseOrderRepository;


@Service
public class PurchaseOrderService {

   @Autowired
   private PurchaseOrderRepository poRepo;

   @Autowired
   private LineItemRepository liRepo;

	@Autowired @Qualifier("registrationCache")
    private RedisTemplate<String, String> template;

    @Autowired @Qualifier("poPubSub")
    private ChannelTopic topic;

	public boolean createPurchaseOrderManualTx(PurchaseOrder po) {
        template.convertAndSend(
            topic.getTopic(),
            po.toJSON().toString()
        ); 
		return false;
	}

    public String[] getAllRegisteredCustomer(){
        return template.opsForList().range("registration", 0, -1).toArray(new String[0]);
    }

   @Transactional(rollbackFor = PurchaseOrderException.class)
   public boolean createPurchaseOrder(PurchaseOrder po) throws PurchaseOrderException {
      String poId = UUID.randomUUID().toString().substring(0, 8);
      System.out.printf(">>>>> poId: %s\n", poId);
      po.setOrderId(poId);
      try {
         return poRepo.insertPurchaseOrder(po) 
               && liRepo.insertLineItems(poId, po.getLineItems());
      } catch (PurchaseOrderException ex) {
         System.out.println("----- exception occured");
         throw ex;
      }
   }
}