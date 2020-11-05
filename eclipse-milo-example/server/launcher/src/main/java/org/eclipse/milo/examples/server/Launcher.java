package org.eclipse.milo.examples.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;

public class Launcher {

    static {
        try {
            InputStream is = Launcher.class.getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        waitFinish(Server.launch()).get();
    }

    static CompletableFuture<Void> waitFinish(final Server server) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.shutdown();
            } catch (Exception e) {
                System.err.print("Ha sido imposible detener el servidor de forma ordenada!");
                e.printStackTrace();
            }
            future.complete(null);
        }));
        return future;
    }

}
