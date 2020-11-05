package org.eclipse.milo.examples.domain.helloworld.type.custonenum;

import org.eclipse.milo.examples.common.types.CustomEnumType;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

public class CustomEnumTypeFactory {

    static public void register(NamespaceContext namespaceContext) throws Exception {

        NodeId dataTypeId = CustomEnumType.TYPE_ID
                .localOrThrow(namespaceContext.getServer().getNamespaceTable());

        namespaceContext.getDictionaryManager().registerEnumCodec(
                new CustomEnumType.Codec().asBinaryCodec(),
                "CustomEnumType",
                dataTypeId
        );

        EnumField[] fields = new EnumField[]{
                new EnumField(
                        0L,
                        LocalizedText.english("Field0"),
                        LocalizedText.NULL_VALUE,
                        "Field0"
                ),
                new EnumField(
                        1L,
                        LocalizedText.english("Field1"),
                        LocalizedText.NULL_VALUE,
                        "Field1"
                ),
                new EnumField(
                        2L,
                        LocalizedText.english("Field2"),
                        LocalizedText.NULL_VALUE,
                        "Field2"
                )
        };

        EnumDefinition definition = new EnumDefinition(fields);

        EnumDescription description = new EnumDescription(
                dataTypeId,
                new QualifiedName(namespaceContext.getNamespaceIndex(), "CustomEnumType"),
                definition,
                ubyte(BuiltinDataType.Int32.getTypeId())
        );

        namespaceContext.getDictionaryManager().registerEnumDescription(description);
    }

    final private NamespaceContext namespaceContext;

    public CustomEnumTypeFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new CustomEnumTypeInstance(namespaceContext, parentFolderNode);
    }

}
