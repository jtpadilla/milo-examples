package org.eclipse.milo.examples.server;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.examples.server.builder.Builder;
import org.eclipse.milo.examples.server.namespace.Namespace;
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

        // Se crea el servidor OPC UA
        final OpcUaServer server = new OpcUaServer(Builder.buildConfig());

        // Se crea el Namespace
        final ManagedNamespaceWithLifecycle namespace = new Namespace(server);

        // Se inicia primero en Namespace
        namespace.startup();

        // Se inicia despues del servidor OPC UA (y se espara asincronamente)
        server.startup().get();

        // Se retorna el Wrapper del conjunto para poder paralo despues
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
