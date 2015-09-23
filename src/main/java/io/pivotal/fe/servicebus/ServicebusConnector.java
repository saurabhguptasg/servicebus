package io.pivotal.fe.servicebus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.utils.ConnectionStringSyntaxException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.implementation.ServiceBusConnectionString;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author sgupta
 * @since 9/9/15.
 */
public class ServicebusConnector {

  private ServiceBusContract serviceBusContract;

  public ServicebusConnector() throws IOException, ConnectionStringSyntaxException {
    String queueConnectionString = null;
    String vcap_services = System.getenv("VCAP_SERVICES");
    if (vcap_services != null && vcap_services.length() > 0) {
      // parsing rediscloud credentials
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(vcap_services);
      ArrayNode credentials = (ArrayNode) root.get("user-provided");
      for (JsonNode credential : credentials) {
        ObjectNode credentialNode = (ObjectNode) credential.get("credentials");
        if(credentialNode.has("queueConnectionString")) {
          queueConnectionString = credentialNode.get("queueConnectionString").asText();
          break;
        }
      }
    }
    if(queueConnectionString != null) {
      ServiceBusConnectionString sbcs = new ServiceBusConnectionString(queueConnectionString);

      String endpoint = sbcs.getEndpoint();
      String url = endpoint.substring(5);
      int dot = url.indexOf('.');
      String namespace = url.substring(0, dot);
      String endpointUrl = url.substring(dot);


      Configuration configuration =
          ServiceBusConfiguration.configureWithSASAuthentication(namespace,
                                                                 sbcs.getSharedAccessKeyName(),
                                                                 sbcs.getSharedAccessKey(),
                                                                 endpointUrl);


      serviceBusContract = ServiceBusService.create(configuration);

      System.out.println("serviceBusContract = " + serviceBusContract);
    }
  }

  public ServiceBusContract getServiceBusContract() {
    return serviceBusContract;
  }
}

/*
{
 "VCAP_SERVICES": {
  "user-provided": [
   {
    "credentials": {
     "connectionString": "Data Source=hqf86ai9y5.database.windows.net;Integrated Security=False;User ID=pivotal;Password=3edc$RFV;Connect Timeout=15;Encrypt=False;TrustServerCertificate=False;ApplicationIntent=ReadWrite;MultiSubnetFailover=False;Initial Catalog=ContosoUniversity2"
    },
    "label": "user-provided",
    "name": "contoso-demo-sql",
    "syslog_drain_url": "",
    "tags": []
   },
   {
    "credentials": {
     "queueConnectionString": "Endpoint=sb://sgupta-pivotal.servicebus.windows.net/;SharedAccessKeyName=read-write;SharedAccessKey=JWnh4eH+q7F9lUMFlI4+2hcT/TDCq7iaid6u9osS3XE="
    },
    "label": "user-provided",
    "name": "contoso-demo-queue",
    "syslog_drain_url": "",
    "tags": []
   }
  ]
 }
}

* */