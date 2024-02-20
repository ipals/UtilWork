package com.test

import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.BoundedExponentialBackoffRetry
import org.apache.zookeeper.KeeperException.NoNodeException

class ZkMap {
    private static final String CHARSET = 'UTF-8'

    private final String rootPath
    private final CuratorFramework client

    ZkMap(String zkConnectionString, String rootPath) {
        this.rootPath = rootPath
        this.client = CuratorFrameworkFactory.newClient(zkConnectionString, new BoundedExponentialBackoffRetry(1000, 120000, Integer.MAX_VALUE))
        this.client.start()
    }

    def put(String key, String value) {
        def node = "$rootPath/$key"
        def data = value.getBytes(CHARSET)
        if (client.checkExists().forPath(node)) {
            client.setData().forPath(node, data)
        } else {
            client.create().creatingParentsIfNeeded().forPath(node, data)
        }
        node
    }

    def get(String key) {
        try {
            def value = client.data.forPath("$rootPath/$key")
            new String(value, CHARSET)
        } catch (NoNodeException e) {
            return '0'
        }
    }

    def stop() {
        client.close()
    }
}
