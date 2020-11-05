package org.eclipse.milo.examples.domain.helloworld.type.customstruct;

import org.eclipse.milo.examples.common.types.CustomStructType;
import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class CustomStructTypeInstance extends AbstractNodeDomainCloseable {

    CustomStructTypeInstance(NamespaceContext namespaceContext, UaFolderNode parentFolderNode) throws Exception {

        super(namespaceContext);

        NodeId dataTypeId = CustomStructType.TYPE_ID
                .localOrThrow(getServer().getNamespaceTable());

        NodeId binaryEncodingId = CustomStructType.BINARY_ENCODING_ID
                .localOrThrow(getServer().getNamespaceTable());

        UaVariableNode customStructTypeVariable = UaVariableNode.builder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/CustomStructTypeVariable"))
                .setAccessLevel(AccessLevel.READ_WRITE)
                .setUserAccessLevel(AccessLevel.READ_WRITE)
                .setBrowseName(newQualifiedName("CustomStructTypeVariable"))
                .setDisplayName(LocalizedText.english("CustomStructTypeVariable"))
                .setDataType(dataTypeId)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        CustomStructType value = new CustomStructType(
                "foo",
                uint(42),
                true
        );

        ExtensionObject xo = ExtensionObject.encodeDefaultBinary(
                getServer().getSerializationContext(),
                value,
                binaryEncodingId
        );

        customStructTypeVariable.setValue(new DataValue(new Variant(xo)));

        getNodeManager().addNode(customStructTypeVariable);

        customStructTypeVariable.addReference(new Reference(
                customStructTypeVariable.getNodeId(),
                Identifiers.Organizes,
                parentFolderNode.getNodeId().expanded(),
                false
        ));

    }

}
