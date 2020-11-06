package org.eclipse.milo.examples.util;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.api.DataTypeDictionaryManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.UUID;

abstract public class AbstractNodeDomain implements NamespaceContext {

    final private NamespaceContext namespaceContext;

    public AbstractNodeDomain(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public NamespaceContext getNamespaceContext() {
        return namespaceContext;
    }

    @Override
    public UaNodeManager getNodeManager() {
        return namespaceContext.getNodeManager();
    }

    @Override
    public UaNodeContext getNodeContext() {
        return namespaceContext.getNodeContext();
    }

    @Override
    public NodeFactory getNodeFactory() {
        return namespaceContext.getNodeFactory();
    }

    @Override
    public NodeId newNodeId(String id) {
        return namespaceContext.newNodeId(id);
    }

    @Override
    public NodeId newNodeId(UUID uuid) {
        return namespaceContext.newNodeId(uuid);
    }

    @Override
    public NodeId newNodeId(long id) {
        return namespaceContext.newNodeId(id);
    }

    @Override
    public NodeId newNodeId(UInteger id) {
        return namespaceContext.newNodeId(id);
    }

    @Override
    public NodeId newNodeId(ByteString id) {
        return namespaceContext.newNodeId(id);
    }

    @Override
    public QualifiedName newQualifiedName(String name) {
        return namespaceContext.newQualifiedName(name);
    }

    @Override
    public OpcUaServer getServer() {
        return namespaceContext.getServer();
    }

    @Override
    public DataTypeDictionaryManager getDictionaryManager() {
        return namespaceContext.getDictionaryManager();
    }

    @Override
    public UShort getNamespaceIndex() {
        return namespaceContext.getNamespaceIndex();
    }

}
