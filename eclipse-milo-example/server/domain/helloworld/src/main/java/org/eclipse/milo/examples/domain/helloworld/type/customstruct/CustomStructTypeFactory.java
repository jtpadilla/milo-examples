package org.eclipse.milo.examples.domain.helloworld.type.customstruct;

import org.eclipse.milo.examples.common.types.CustomStructType;
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

public class CustomStructTypeFactory {

    static public void register(NamespaceContext namespaceContext) throws Exception {

        NodeId dataTypeId = CustomStructType.TYPE_ID
                .localOrThrow(namespaceContext.getServer().getNamespaceTable());

        NodeId binaryEncodingId = CustomStructType.BINARY_ENCODING_ID
                .localOrThrow(namespaceContext.getServer().getNamespaceTable());

        // At a minimum, custom types must have their codec registered.
        // If clients don't need to dynamically discover types and will
        // register the codecs on their own then this is all that is
        // necessary.
        // The dictionary manager will add a corresponding DataType Node to
        // the AddressSpace.

        namespaceContext.getDictionaryManager().registerStructureCodec(
                new CustomStructType.Codec().asBinaryCodec(),
                "CustomStructType",
                dataTypeId,
                binaryEncodingId
        );

        // If the custom type also needs to be discoverable by clients then it
        // needs an entry in a DataTypeDictionary that can be read by those
        // clients. We describe the type using StructureDefinition or
        // EnumDefinition and register it with the dictionary manager.
        // The dictionary manager will add all the necessary nodes to the
        // AddressSpace and generate the required dictionary bsd.xml file.

        StructureField[] fields = new StructureField[]{
                new StructureField(
                        "foo",
                        LocalizedText.NULL_VALUE,
                        Identifiers.String,
                        ValueRanks.Scalar,
                        null,
                        namespaceContext.getServer().getConfig().getLimits().getMaxStringLength(),
                        false
                ),
                new StructureField(
                        "bar",
                        LocalizedText.NULL_VALUE,
                        Identifiers.UInt32,
                        ValueRanks.Scalar,
                        null,
                        uint(0),
                        false
                ),
                new StructureField(
                        "baz",
                        LocalizedText.NULL_VALUE,
                        Identifiers.Boolean,
                        ValueRanks.Scalar,
                        null,
                        uint(0),
                        false
                )
        };

        StructureDefinition definition = new StructureDefinition(
                binaryEncodingId,
                Identifiers.Structure,
                StructureType.Structure,
                fields
        );

        StructureDescription description = new StructureDescription(
                dataTypeId,
                new QualifiedName(namespaceContext.getNamespaceIndex(), "CustomStructType"),
                definition
        );

        namespaceContext.getDictionaryManager().registerStructureDescription(description, binaryEncodingId);

    }

    final private NamespaceContext namespaceContext;

    public CustomStructTypeFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new CustomStructTypeInstance(namespaceContext, parentFolderNode);
    }

}
