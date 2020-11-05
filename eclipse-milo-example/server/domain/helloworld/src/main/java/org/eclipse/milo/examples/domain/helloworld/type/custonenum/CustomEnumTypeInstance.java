package org.eclipse.milo.examples.domain.helloworld.type.custonenum;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

class CustomEnumTypeInstance extends AbstractNodeDomainCloseable {

    CustomEnumTypeInstance(NamespaceContext namespaceContext, UaFolderNode parentFolderNode) throws Exception {

        super(namespaceContext);

        NodeId dataTypeId = org.eclipse.milo.examples.common.types.CustomEnumType.TYPE_ID
                .localOrThrow(getServer().getNamespaceTable());

        UaVariableNode customEnumTypeVariable = UaVariableNode.builder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/CustomEnumTypeVariable"))
                .setAccessLevel(AccessLevel.READ_WRITE)
                .setUserAccessLevel(AccessLevel.READ_WRITE)
                .setBrowseName(newQualifiedName("CustomEnumTypeVariable"))
                .setDisplayName(LocalizedText.english("CustomEnumTypeVariable"))
                .setDataType(dataTypeId)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        customEnumTypeVariable.setValue(new DataValue(new Variant(org.eclipse.milo.examples.common.types.CustomEnumType.Field1)));

        getNodeManager().addNode(customEnumTypeVariable);

        customEnumTypeVariable.addReference(new Reference(
                customEnumTypeVariable.getNodeId(),
                Identifiers.Organizes,
                parentFolderNode.getNodeId().expanded(),
                false

        ));

    }

}

