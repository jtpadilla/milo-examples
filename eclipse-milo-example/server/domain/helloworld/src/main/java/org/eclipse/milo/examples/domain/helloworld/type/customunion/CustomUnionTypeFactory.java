package org.eclipse.milo.examples.domain.helloworld.type.customunion;

import org.eclipse.milo.examples.common.types.CustomUnionType;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class CustomUnionTypeFactory {

    static public void register(NamespaceContext namespaceContext) throws Exception {

        NodeId dataTypeId = CustomUnionType.TYPE_ID
                .localOrThrow(namespaceContext.getServer().getNamespaceTable());

        NodeId binaryEncodingId = CustomUnionType.BINARY_ENCODING_ID
                .localOrThrow(namespaceContext.getServer().getNamespaceTable());

        namespaceContext.getDictionaryManager().registerUnionCodec(
                new CustomUnionType.Codec().asBinaryCodec(),
                "CustomUnionType",
                dataTypeId,
                binaryEncodingId
        );

        StructureField[] fields = new StructureField[]{
                new StructureField(
                        "foo",
                        LocalizedText.NULL_VALUE,
                        Identifiers.UInt32,
                        ValueRanks.Scalar,
                        null,
                        namespaceContext.getServer().getConfig().getLimits().getMaxStringLength(),
                        false
                ),
                new StructureField(
                        "bar",
                        LocalizedText.NULL_VALUE,
                        Identifiers.String,
                        ValueRanks.Scalar,
                        null,
                        uint(0),
                        false
                )
        };

        StructureDefinition definition = new StructureDefinition(
                binaryEncodingId,
                Identifiers.Structure,
                StructureType.Union,
                fields
        );

        StructureDescription description = new StructureDescription(
                dataTypeId,
                new QualifiedName(namespaceContext.getNamespaceIndex(), "CustomUnionType"),
                definition
        );

        namespaceContext.getDictionaryManager().registerStructureDescription(description, binaryEncodingId);

    }

    final private NamespaceContext namespaceContext;

    public CustomUnionTypeFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new CustomUnionTypeInstance(namespaceContext, parentFolderNode);
    }

}
