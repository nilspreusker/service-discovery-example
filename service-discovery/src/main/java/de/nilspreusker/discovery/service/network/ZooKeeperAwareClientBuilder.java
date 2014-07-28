package de.nilspreusker.discovery.service.network;

import de.nilspreusker.discovery.service.InstanceDetails;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class ZooKeeperAwareClientBuilder {

    @Inject
    ServiceDiscovery<InstanceDetails> serviceDiscovery;

    public ResteasyClient build() {
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                try {
                    return getServiceUrl(host);
                } catch (UnknownHostException e) {
                    return super.resolve(host);
                }
            }
        };
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, new ZooKeeperAwareSocketFactory(serviceDiscovery)));
        final PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(
                registry,
                dnsResolver);
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager);
        ApacheHttpClient4Engine httpEngine = new ApacheHttpClient4Engine(httpClient);
        return new ResteasyClientBuilder().httpEngine(httpEngine).build();
    }

    private InetAddress[] getServiceUrl(String host) throws UnknownHostException {
        try {
            Collection<ServiceInstance<InstanceDetails>> instances = serviceDiscovery.queryForInstances(host);
            if (null == instances || instances.size() == 0) {
                throw new UnknownHostException("No instances of " + host + " registered in service registry");
            }
            List<InetAddress> inetAddresses = new ArrayList<>(instances.size());
            for (ServiceInstance<InstanceDetails> instance : instances) {
                inetAddresses.add(InetAddress.getByName(instance.getAddress()));
            }
            return inetAddresses.toArray(new InetAddress[inetAddresses.size()]);
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve service instance for host '" + host + "'", e);
        }
    }
}
