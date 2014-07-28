package de.nilspreusker.provider.boundary.discovery;

import de.nilspreusker.discovery.service.Discovery;
import de.nilspreusker.discovery.service.InstanceDetails;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
@WebListener
public class ServiceRegistryContextListener implements ServletContextListener {

    private ServiceInstance<InstanceDetails> serviceInstance;
    private ServiceDiscovery<InstanceDetails> serviceDiscovery;

    @Inject
    CuratorFramework client;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        int port = Integer.parseInt(System.getProperty("jboss.http.port"));
        UriSpec uriSpec = new UriSpec("{scheme}://cars.mycompany.com:{port}");
        try {
            serviceInstance = ServiceInstance.<InstanceDetails>builder()
                    .name("cars.mycompany.com")
                    .payload(new InstanceDetails(1l))
                    .port(port)
                    .uriSpec(uriSpec)
                    .build();
            InstanceSerializer<InstanceDetails> instanceSerializer = new JsonInstanceSerializer<>(InstanceDetails.class);
            serviceDiscovery = ServiceDiscoveryBuilder.<InstanceDetails>builder(InstanceDetails.class)
                    .client(client)
                    .basePath(Discovery.SERVICES_BASE_PATH)
                    .serializer(instanceSerializer)
                    .thisInstance(serviceInstance)
                    .build();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException("Unable to register car service", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
