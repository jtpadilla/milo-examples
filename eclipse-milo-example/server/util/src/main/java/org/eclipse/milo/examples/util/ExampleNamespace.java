package org.eclipse.milo.examples.util;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.api.DataTypeDictionaryManager;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.UUID;

abstract public class ExampleNamespace extends ManagedNamespaceWithLifecycle {

    final private DataTypeDictionaryManager dictionaryManager;
    final private NamespaceContext namespaceContext;

    public ExampleNamespace(OpcUaServer server, String namespaceUri) {

        super(server, namespaceUri);

        this.dictionaryManager = new DataTypeDictionaryManager(getNodeContext(), namespaceUri);

        this.namespaceContext = new NamespaceContext() {

            @Override
            public UaNodeManager getNodeManager() {
                return ExampleNamespace.this.getNodeManager();
            }

            @Override
            public UaNodeContext getNodeContext() {
                return ExampleNamespace.this.getNodeContext();
            }

            @Override
            public NodeId newNodeId(String id) {
                return ExampleNamespace.this.newNodeId(id);
            }

            @Override
            public NodeId newNodeId(UUID uuid) {
                return ExampleNamespace.this.newNodeId(uuid);
            }

            @Override
            public QualifiedName newQualifiedName(String name) {
                return ExampleNamespace.this.newQualifiedName(name);
            }

            @Override
            public NodeFactory getNodeFactory() {
                return ExampleNamespace.this.getNodeFactory();
            }

            @Override
            public OpcUaServer getServer() {
                return ExampleNamespace.this.getServer();
            }

            @Override
            public DataTypeDictionaryManager getDictionaryManager() {
                return dictionaryManager;
            }

            @Override
            public UShort getNamespaceIndex() {
                return ExampleNamespace.this.getNamespaceIndex();
            }

        };

    }

    protected DataTypeDictionaryManager getDictionaryManager() {
        return dictionaryManager;
    }

    protected NamespaceContext getNamespaceContext() {
        return namespaceContext;
    }

}
