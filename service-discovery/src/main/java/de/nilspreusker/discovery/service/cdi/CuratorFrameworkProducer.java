package de.nilspreusker.discovery.service.cdi;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
public class CuratorFrameworkProducer {

    @Resource(lookup = "java:jboss/ee/concurrency/factory/default")
    private ManagedThreadFactory defaultManagedThreadFactory;

    private CuratorFramework client;

    @Produces
    @ApplicationScoped
    public CuratorFramework produceCuratorFramework() {
        if (null == client) {
            String connectionString = System.getenv("ZK_CONNECTION_STRING");
            if (null == connectionString || connectionString.length() < 1) {
                throw new RuntimeException("ZK_CONNECTION_STRING not found in environment variables.");
            }
            client = CuratorFrameworkFactory
                    .builder()
                    .connectString(connectionString)
                    .sessionTimeoutMs(60 * 1000)
                    .connectionTimeoutMs(15 * 1000)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .threadFactory(defaultManagedThreadFactory)
                    .build();
            client.start();
        }
        return client;
    }

    public void closeCuratorFramework(@Disposes CuratorFramework client) {
        CloseableUtils.closeQuietly(client);
        // Nils Preusker, 16.6.2014: Waiting for 1 second will (in most cases) prevent
        // "java.lang.NoClassDefFoundError: org/apache/zookeeper/server/ZooTrace" from occurring when un-deploying
        // the application. It would be better to wait for ZooKeepers "ClientCnxn" class to finish closing the
        // connection, but there doesn't seem to be a notification or synchronization mechanism for this.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

}
