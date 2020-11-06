package org.eclipse.milo.examples.server.namespace;

import org.eclipse.milo.examples.common.types.NamespaceConstants;
import org.eclipse.milo.examples.domain.helloworld.HelloWorldContainer;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.ExampleNamespace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;

import java.util.List;

public class Namespace extends ExampleNamespace {

    static final public String NAMESPACE_URI = NamespaceConstants.NAMESPACE_URI;

    static public Namespace instantiate(OpcUaServer server) {
        return new Namespace(server);
    }

    final private SubscriptionModel subscriptionModel;

    private DomainCloseable helloWorldContainer;

    public Namespace(OpcUaServer server) {

        super(server, NAMESPACE_URI);

        subscriptionModel = new SubscriptionModel(server, this);

        getLifecycleManager().addLifecycle(getDictionaryManager());
        getLifecycleManager().addLifecycle(subscriptionModel);

        // Se lanzan los contenedores principales
        HelloWorldContainer.instantiate(getNamespaceContext(), getLifecycleManager());

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
