package org.eclipse.milo.examples.util;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.api.DataTypeDictionaryManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.UUID;

public interface NamespaceContext {
    UaNodeManager getNodeManager();
    UaNodeContext getNodeContext();
    NodeFactory getNodeFactory();
    NodeId newNodeId(String id);
    NodeId newNodeId(UUID id);
    QualifiedName newQualifiedName(String name);
    OpcUaServer getServer();
    DataTypeDictionaryManager getDictionaryManager();
    UShort getNamespaceIndex();
}
