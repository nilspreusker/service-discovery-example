package de.nilspreusker.discovery.service.cdi;

import de.nilspreusker.discovery.service.Discovery;
import de.nilspreusker.discovery.service.InstanceDetails;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class ServiceDiscoveryProducer {

    private ServiceDiscovery<InstanceDetails> serviceDiscovery;

    @Inject
    CuratorFramework client;

    private void init() {
        JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<>(InstanceDetails.class);
        serviceDiscovery = ServiceDiscoveryBuilder
                .builder(InstanceDetails.class)
                .client(client)
                .serializer(serializer)
                .basePath(Discovery.SERVICES_BASE_PATH)
                .build();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException("Error starting service discovery", e);
        }
    }

    @Produces
    ServiceDiscovery<InstanceDetails> getServiceDiscovery() {
        if (null == serviceDiscovery) {
            init();
        }
        return serviceDiscovery;
    }

    @PreDestroy
    public void cleanUp() {
        CloseableUtils.closeQuietly(serviceDiscovery);
    }
}
