package org.eclipse.milo.examples.server;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.examples.server.builder.Builder;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;
import java.util.concurrent.ExecutionException;

public class Server {

    static private final Logger logger = LoggerFactory.getLogger(Server.class);

    static {
        // Required for SecurityPolicy.Aes256_Sha256_RsaPss
        Security.addProvider(new BouncyCastleProvider());
    }

    static public Server launch() throws Exception {
        OpcUaServerConfig config = Builder.buildConfig();
        final OpcUaServer server = new OpcUaServer(config);
        final ManagedNamespaceWithLifecycle namespace = new Namespace(server);
        namespace.startup();
        server.startup().get();
        return new Server(server, namespace);
    }

    private final OpcUaServer server;
    private final ManagedNamespaceWithLifecycle namespace;

    public Server(OpcUaServer server, ManagedNamespaceWithLifecycle namespace) {
        this.server = server;
        this.namespace = namespace;
    }

    public void shutdown() throws ExecutionException, InterruptedException {
        namespace.shutdown();
        server.shutdown().get();
    }

}
