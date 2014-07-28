package de.nilspreusker.discovery.service.network;

import de.nilspreusker.discovery.service.InstanceDetails;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class ZooKeeperAwareSocketFactory extends PlainSocketFactory {

    private ServiceDiscovery<InstanceDetails> serviceDiscovery;

    public ZooKeeperAwareSocketFactory(ServiceDiscovery<InstanceDetails> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress,
                                HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        String hostName = ((HttpInetSocketAddress) remoteAddress).getHttpHost().getHostName();
        try {
            Collection<ServiceInstance<InstanceDetails>> instances = serviceDiscovery.queryForInstances(hostName);
            for (ServiceInstance<InstanceDetails> instance : instances) {
                if (instance.getAddress().equals(remoteAddress.getAddress().getHostAddress())) {
                    InetSocketAddress newRemoteAddress = new InetSocketAddress(remoteAddress.getAddress(),
                            instance.getPort());
                    InetSocketAddress newLocalAddress = null;
                    if (null != localAddress) {
                        newLocalAddress = new InetSocketAddress(localAddress.getAddress(), instance.getPort());
                    }
                    return super.connectSocket(socket, newRemoteAddress, newLocalAddress, params);
                }
            }
        } catch (Exception e) {
            throw new UnknownHostException("Could not find host '" + hostName + "' in service registry");
        }
        return super.connectSocket(socket, remoteAddress, localAddress, params);
    }
}
