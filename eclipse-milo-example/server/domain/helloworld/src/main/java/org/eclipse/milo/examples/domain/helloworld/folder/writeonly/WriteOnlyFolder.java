package org.eclipse.milo.examples.domain.helloworld.folder.writeonly;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.UUID;

public class WriteOnlyFolder extends AbstractNodeDomainCloseable {

    private UaFolderNode folderNode;

    public WriteOnlyFolder(NamespaceContext namespaceContext, UaFolderNode parentFolderNode, UUID uuid) {

        super(namespaceContext);

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(uuid),
                newQualifiedName("WriteOnly"),
                LocalizedText.english("WriteOnly")
        );

        getNodeManager().addNode(this.folderNode);
        parentFolderNode.addOrganizes(this.folderNode);

        addVariables();

    }

    private void addVariables() {

        String name = "String";
        UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/WriteOnly/" + name))
                .setAccessLevel(AccessLevel.WRITE_ONLY)
                .setUserAccessLevel(AccessLevel.WRITE_ONLY)
                .setBrowseName(newQualifiedName(name))
                .setDisplayName(LocalizedText.english(name))
                .setDataType(Identifiers.String)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        node.setValue(new DataValue(new Variant("can't read this")));

        getNodeManager().addNode(node);
        folderNode.addOrganizes(node);

    }

    @Override
    public void uninstall() {

    }

}
