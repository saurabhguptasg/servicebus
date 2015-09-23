package io.pivotal.fe.servicebus;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author sgupta
 * @since 9/9/15.
 */
@Controller
@RequestMapping(value = "/sb/**", produces = "application/json")
public class ServicebusController {
  public static final Random RANDOM = new Random();

  private JsonNodeFactory jsonFactory = JsonNodeFactory.instance;

  @Autowired
  ServicebusConnector connector;

  @RequestMapping(value = "/send")
  @ResponseBody
  public ArrayNode sendMessage() throws ServiceException, UnsupportedEncodingException {
    ArrayNode list = new ArrayNode(jsonFactory);
    for (int i=0; i < 10; i++) {
      ObjectNode map = new ObjectNode(jsonFactory);
      String messageId = Long.toString(System.currentTimeMillis(), 36);
      String timestamp = Long.toString(System.currentTimeMillis());
      String message = Long.toString(RANDOM.nextLong(), 36) + Long.toString(RANDOM.nextLong(), 36);
      map.put("messageId", messageId);
      map.put("timestamp", timestamp);
      map.put("message", message);

      BrokeredMessage brokeredMessage = new BrokeredMessage();
      brokeredMessage.setBody(new ByteArrayInputStream(map.toString().getBytes("UTF-8")));

      connector.getServiceBusContract().sendQueueMessage("contoso", brokeredMessage);

      list.add(map);
    }

    return list;
  }
}
