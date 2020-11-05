package org.eclipse.milo.examples.domain.helloworld.task.boguseventnotifier;

import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.model.nodes.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.nodes.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

public class BogusEventNotifierTask implements Lifecycle {

    static public Lifecycle instantiate(NamespaceContext namespaceContext) throws Exception {
        return new BogusEventNotifierTask(namespaceContext);
    }

    final private Logger logger = LoggerFactory.getLogger(getClass());

    final private Random random = new Random();

    final private NamespaceContext namespaceContext;

    private volatile Thread eventThread;
    private volatile boolean keepPostingEvents = true;

    public BogusEventNotifierTask(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    @Override
    public void startup() {

        // Set the EventNotifier bit on Server Node for Events.
        UaNode serverNode = namespaceContext.getServer()
                .getAddressSpaceManager()
                .getManagedNode(Identifiers.Server)
                .orElse(null);

        if (serverNode instanceof ServerTypeNode) {
            ((ServerTypeNode) serverNode).setEventNotifier(ubyte(1));

            // Post a bogus Event every couple seconds
            eventThread = new Thread(() -> {
                while (keepPostingEvents) {
                    try {
                        BaseEventTypeNode eventNode = namespaceContext.getServer().getEventFactory().createEvent(
                                namespaceContext.newNodeId(UUID.randomUUID()),
                                Identifiers.BaseEventType
                        );

                        eventNode.setBrowseName(new QualifiedName(1, "foo"));
                        eventNode.setDisplayName(LocalizedText.english("foo"));
                        eventNode.setEventId(ByteString.of(new byte[]{0, 1, 2, 3}));
                        eventNode.setEventType(Identifiers.BaseEventType);
                        eventNode.setSourceNode(serverNode.getNodeId());
                        eventNode.setSourceName(serverNode.getDisplayName().getText());
                        eventNode.setTime(DateTime.now());
                        eventNode.setReceiveTime(DateTime.NULL_VALUE);
                        eventNode.setMessage(LocalizedText.english("event message!"));
                        eventNode.setSeverity(ushort(2));

                        //noinspection UnstableApiUsage
                        namespaceContext.getServer().getEventBus().post(eventNode);

                        eventNode.delete();
                    } catch (Throwable e) {
                        logger.error("Error creating EventNode: {}", e.getMessage(), e);
                    }

                    try {
                        //noinspection BusyWait
                        Thread.sleep(2_000);
                    } catch (InterruptedException ignored) {
                        // ignored
                    }
                }
            }, "bogus-event-poster");

            eventThread.start();
        }

    }

    @Override
    public void shutdown() {
        try {
            keepPostingEvents = false;
            eventThread.interrupt();
            eventThread.join();
        } catch (InterruptedException ignored) {
            // ignored
        }
    }

}
