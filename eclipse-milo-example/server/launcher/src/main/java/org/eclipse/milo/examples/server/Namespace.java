package org.eclipse.milo.examples.server;

import org.eclipse.milo.examples.common.types.NamespaceConstants;
import org.eclipse.milo.examples.domain.helloworld.HelloWorldFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.task.boguseventnotifier.BogusEventNotifierTask;
import org.eclipse.milo.examples.domain.helloworld.type.customstruct.CustomStructTypeFactory;
import org.eclipse.milo.examples.domain.helloworld.type.customunion.CustomUnionTypeFactory;
import org.eclipse.milo.examples.domain.helloworld.type.custonenum.CustomEnumTypeFactory;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.DataTypeDictionaryManager;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.List;
import java.util.UUID;

public class Namespace extends ManagedNamespaceWithLifecycle {

    static final public String NAMESPACE_URI = NamespaceConstants.NAMESPACE_URI;

    final private DataTypeDictionaryManager dictionaryManager;
    final private SubscriptionModel subscriptionModel;
    final private NamespaceContext namespaceContext;

    // Builders
    final private HelloWorldFolderFactory helloWorldFolderFactory;

    // Nodos principales
    private DomainCloseable helloWorldFolder;

    public Namespace(OpcUaServer server) throws Exception {

        super(server, NAMESPACE_URI);

        subscriptionModel = new SubscriptionModel(server, this);
        dictionaryManager = new DataTypeDictionaryManager(getNodeContext(), NAMESPACE_URI);

        getLifecycleManager().addLifecycle(dictionaryManager);
        getLifecycleManager().addLifecycle(subscriptionModel);

        this.namespaceContext = new NamespaceContext() {

            @Override
            public UaNodeManager getNodeManager() {
                return Namespace.this.getNodeManager();
            }

            @Override
            public UaNodeContext getNodeContext() {
                return Namespace.this.getNodeContext();
            }

            @Override
            public NodeId newNodeId(String id) {
                return Namespace.this.newNodeId(id);
            }

            @Override
            public NodeId newNodeId(UUID uuid) {
                return Namespace.this.newNodeId(uuid);
            }

            @Override
            public QualifiedName newQualifiedName(String name) {
                return Namespace.this.newQualifiedName(name);
            }

            @Override
            public NodeFactory getNodeFactory() {
                return Namespace.this.getNodeFactory();
            }

            @Override
            public OpcUaServer getServer() {
                return Namespace.this.getServer();
            }

            @Override
            public DataTypeDictionaryManager getDictionaryManager() {
                return Namespace.this.dictionaryManager;
            }

            @Override
            public UShort getNamespaceIndex() {
                return Namespace.this.getNamespaceIndex();
            }

        };

        // Secrean los builders
        this.helloWorldFolderFactory = new HelloWorldFolderFactory(namespaceContext);

        // se instalan los builders
        getLifecycleManager().addStartupTask(this::createAndAddNodes);

        getLifecycleManager().addLifecycle(BogusEventNotifierTask.instantiate(namespaceContext));

    }

    private void createAndAddNodes() {

        try {
            // Se registra los tipos
            CustomEnumTypeFactory.register(namespaceContext);
            CustomStructTypeFactory.register(namespaceContext);
            CustomUnionTypeFactory.register(namespaceContext);

            // Se crean las carpetas principales
            this.helloWorldFolder = helloWorldFolderFactory.instantiate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
        subscriptionModel.onMonitoringModeChanged(monitoredItems);
    }

}
