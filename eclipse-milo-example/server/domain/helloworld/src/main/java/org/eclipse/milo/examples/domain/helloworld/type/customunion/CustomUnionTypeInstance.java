package org.eclipse.milo.examples.domain.helloworld.type.customunion;

import org.eclipse.milo.examples.common.types.CustomUnionType;
import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;

public class CustomUnionTypeInstance  extends AbstractNodeDomainCloseable {

    CustomUnionTypeInstance(NamespaceContext namespaceContext, UaFolderNode parentFolderNode) throws Exception {

        super(namespaceContext);

        NodeId dataTypeId = CustomUnionType.TYPE_ID
                .localOrThrow(getServer().getNamespaceTable());

        NodeId binaryEncodingId = CustomUnionType.BINARY_ENCODING_ID
                .localOrThrow(getServer().getNamespaceTable());

        UaVariableNode customUnionTypeVariable = UaVariableNode.builder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/CustomUnionTypeVariable"))
                .setAccessLevel(AccessLevel.READ_WRITE)
                .setUserAccessLevel(AccessLevel.READ_WRITE)
                .setBrowseName(newQualifiedName("CustomUnionTypeVariable"))
                .setDisplayName(LocalizedText.english("CustomUnionTypeVariable"))
                .setDataType(dataTypeId)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        CustomUnionType value = CustomUnionType.ofBar("hello");

        ExtensionObject xo = ExtensionObject.encodeDefaultBinary(
                getServer().getSerializationContext(),
                value,
                binaryEncodingId
        );

        customUnionTypeVariable.setValue(new DataValue(new Variant(xo)));

        getNodeManager().addNode(customUnionTypeVariable);

        customUnionTypeVariable.addReference(new Reference(
                customUnionTypeVariable.getNodeId(),
                Identifiers.Organizes,
                parentFolderNode.getNodeId().expanded(),
                false
        ));

    }

}
