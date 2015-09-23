package io.pivotal.fe.servicebus;

import com.microsoft.windowsazure.core.utils.ConnectionStringSyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@SpringBootApplication
@EnableAutoConfiguration
public class ServicebusApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicebusApplication.class, args);
    }

  @Bean
  @Scope(value = "singleton")
  public ServicebusConnector getServicebusConnector() throws IOException, ConnectionStringSyntaxException {
    return new ServicebusConnector();
  }
}
