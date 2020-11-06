package org.eclipse.milo.examples.domain.helloworld.folder.dynamic;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.AttributeLoggingFilter;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.Random;
import java.util.UUID;

public class DynamicFolder extends AbstractNodeDomainCloseable {

    final private Random random = new Random();

    private UaFolderNode folderNode;

    public DynamicFolder(NamespaceContext namespaceContext, UaFolderNode parentFolderNode, UUID uuid) {

        super(namespaceContext);

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(uuid),
                newQualifiedName("Dynamic"),
                LocalizedText.english("Dynamic")
        );

        getNodeManager().addNode(this.folderNode);
        parentFolderNode.addOrganizes(this.folderNode);

        addBooleanVariable();
        addInt32Variable();
        addDoubleVariable();

    }

    private void addBooleanVariable() {

        String name = "Boolean";
        NodeId typeId = Identifiers.Boolean;
        Variant variant = new Variant(false);

        UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/Dynamic/" + name))
                .setAccessLevel(AccessLevel.READ_WRITE)
                .setBrowseName(newQualifiedName(name))
                .setDisplayName(LocalizedText.english(name))
                .setDataType(typeId)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        node.setValue(new DataValue(variant));

        node.getFilterChain().addLast(
                new AttributeLoggingFilter(),
                AttributeFilters.getValue(ctx ->new DataValue(new Variant(random.nextBoolean())))
        );

        getNodeManager().addNode(node);
        folderNode.addOrganizes(node);

    }

    private void addInt32Variable() {
        String name = "Int32";
        NodeId typeId = Identifiers.Int32;
        Variant variant = new Variant(0);

        UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/Dynamic/" + name))
                .setAccessLevel(AccessLevel.READ_WRITE)
                .setBrowseName(newQualifiedName(name))
                .setDisplayName(LocalizedText.english(name))
                .setDataType(typeId)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        node.setValue(new DataValue(variant));

        node.getFilterChain().addLast(
                new AttributeLoggingFilter(),
                AttributeFilters.getValue(ctx -> new DataValue(new Variant(random.nextInt())))
        );

        getNodeManager().addNode(node);
        folderNode.addOrganizes(node);
    }

    private void addDoubleVariable() {
        String name = "Double";
        NodeId typeId = Identifiers.Double;
        Variant variant = new Variant(0.0);

        UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/Dynamic/" + name))
                .setAccessLevel(AccessLevel.READ_WRITE)
                .setBrowseName(newQualifiedName(name))
                .setDisplayName(LocalizedText.english(name))
                .setDataType(typeId)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        node.setValue(new DataValue(variant));

        node.getFilterChain().addLast(
                new AttributeLoggingFilter(),
                AttributeFilters.getValue(ctx ->new DataValue(new Variant(random.nextDouble())))
        );

        getNodeManager().addNode(node);
        folderNode.addOrganizes(node);

    }

}
