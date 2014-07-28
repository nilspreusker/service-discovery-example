package de.nilspreusker.consumer.control;

import de.nilspreusker.consumer.entity.CarOrder;
import de.nilspreusker.discovery.service.InstanceDetails;
import de.nilspreusker.discovery.service.network.ZooKeeperAwareClientBuilder;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class CarOrderService {

    @Inject
    ZooKeeperAwareClientBuilder clientBuilder;

    public CarOrder placeCarOrder(CarOrder carOrder) {
        String url = "http://cars.mycompany.com/service-provider/rest/car";

        System.out.println("----------------");
        System.out.println(url);
        System.out.println("----------------");

        ResteasyClient client = clientBuilder.build();
        Response response = client
                .target(url)
                .request()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post(Entity.entity("{\"id\": 1, \"type\": \"convertible\"}", MediaType.APPLICATION_JSON));

        System.out.println("Got HTTP " + response.getStatus() + " when placing car order...");
        System.out.println("Ordered car: " + response.getLocation());

        return carOrder;
    }
}
