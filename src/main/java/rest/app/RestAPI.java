package rest.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@ApplicationPath("api")
@WebListener
public class RestAPI extends ResourceConfig implements ServletContextListener {

	private final static String QUEUE_NAME = "hello";

	public RestAPI() throws IOException, TimeoutException {
		packages("resource");
		System.out.println("hallo");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbit.begon.dev");
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "Test 3!";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			System.out.println(" [x] Sent '" + message + "'");
		}
	}

}

