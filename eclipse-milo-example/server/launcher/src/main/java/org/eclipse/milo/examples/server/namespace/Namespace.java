package org.eclipse.milo.examples.server.namespace;

import org.eclipse.milo.examples.common.types.NamespaceConstants;
import org.eclipse.milo.examples.domain.helloworld.HelloWorldFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.task.boguseventnotifier.BogusEventNotifierTask;
import org.eclipse.milo.examples.domain.helloworld.type.customstruct.CustomStructTypeFactory;
import org.eclipse.milo.examples.domain.helloworld.type.customunion.CustomUnionTypeFactory;
import org.eclipse.milo.examples.domain.helloworld.type.custonenum.CustomEnumTypeFactory;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.ExampleNamespace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;

import java.util.List;

public class Namespace extends ExampleNamespace {

    static final public String NAMESPACE_URI = NamespaceConstants.NAMESPACE_URI;

    final private SubscriptionModel subscriptionModel;

    // Builders
    final private HelloWorldFolderFactory helloWorldFolderFactory;

    // Nodos principales
    private DomainCloseable helloWorldFolder;

    public Namespace(OpcUaServer server) throws Exception {

        super(server, NAMESPACE_URI);

        subscriptionModel = new SubscriptionModel(server, this);

        getLifecycleManager().addLifecycle(getDictionaryManager());
        getLifecycleManager().addLifecycle(subscriptionModel);

        // Secrean los builders
        this.helloWorldFolderFactory = new HelloWorldFolderFactory(getNamespaceContext());

        // se instalan los builders
        getLifecycleManager().addStartupTask(this::createAndAddNodes);

        getLifecycleManager().addLifecycle(BogusEventNotifierTask.instantiate(getNamespaceContext()));

    }

    private void createAndAddNodes() {

        try {
            // Se registra los tipos
            CustomEnumTypeFactory.register(getNamespaceContext());
            CustomStructTypeFactory.register(getNamespaceContext());
            CustomUnionTypeFactory.register(getNamespaceContext());

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
