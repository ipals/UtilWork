package com.test

import com.test.EntitlementFactory
import com.test.tfc.feed.FakeFeedWithZk
import com.test.tfc.feed.Feed
import com.test.web.DebEmulator
import spark.Request
import spark.servlet.SparkApplication

import static spark.Spark.*

class SparkEntryClass implements SparkApplication {
    private final Feed feed
    private final DbEntitlements entitlements
    private final ZkMap zkMap

    SparkEntryClass() {
        def env = System.getProperty('environment')
        feed = new FakeFeedWithZk(ConfigUtils.load('application.properties').readOrExtractConfig(env))
        entitlements = new DbEntitlements(feed.config.readString('entEnv'))

        def zkPath = "${feed.config.readString('zkDataPath')}/${ConfigUtils.getClusterId(feed.config)}"
        zkMap = new ZkMap(feed.config.readOrExtractConfig('zookeeper').readString('connection'), zkPath)
    }

    void init() {
        feed.start()

        port(feed.config.readInt('DebService.port', 4567))
        enableCORS()

        DebEmulator.initialize(feed.config)
        get('/isAllowed', { request, response -> handleDbEntitlementsRequest(request) })
        get('/lastAccessTime', { request, response -> getLastAccessTime(request) })
        post('/lastAccessTime', { request, response -> updateLastAccessTime(request) })
    }

    private boolean handleDbEntitlementsRequest(Request request) {
        def params = request.queryMap().toMap()
        def user = params['user'].first()
        def action = params['action'].first()
        return entitlements.checkPermission(user, action)
    }

    private def getLastAccessTime(Request request) {
        def params = request.queryMap().toMap()
        def user = params['user'].first()
        zkMap.get("lastAccessTime/$user")
    }

    private def updateLastAccessTime(Request request) {
        def params = request.queryMap().toMap()
        def user = params['user'].first()
        def value = String.valueOf(new Date().time)
        def updatedNode = zkMap.put("lastAccessTime/$user", value)
        "${request.raw().getRemoteHost()} updated '$updatedNode' set to $value"
    }

    @Override
    void destroy() {
        EntitlementFactory.shutdown()
        zkMap.stop()
        feed.stop()
    }

    private static void enableCORS() {
        options('/*', { request, response ->
            String accessControlRequestHeaders = request.headers('Access-Control-Request-Headers');
            if (accessControlRequestHeaders != null) {
                response.header('Access-Control-Allow-Headers', accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers('Access-Control-Request-Method');
            if (accessControlRequestMethod != null) {
                response.header('Access-Control-Allow-Methods', accessControlRequestMethod);
            }

            return 'OK'
        })

        before({ request, response ->
            response.header('Access-Control-Allow-Origin', '*')
        })
    }
}
