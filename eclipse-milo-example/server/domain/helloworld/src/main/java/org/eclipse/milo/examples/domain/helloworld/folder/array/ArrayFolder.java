package org.eclipse.milo.examples.domain.helloworld.folder.array;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.AttributeLoggingFilter;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import java.lang.reflect.Array;
import java.util.UUID;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.*;

public class ArrayFolder extends AbstractNodeDomainCloseable {

    private static final Object[][] STATIC_ARRAY_NODES = new Object[][]{
            {"BooleanArray", Identifiers.Boolean, false},
            {"ByteArray", Identifiers.Byte, ubyte(0)},
            {"SByteArray", Identifiers.SByte, (byte) 0x00},
            {"Int16Array", Identifiers.Int16, (short) 16},
            {"Int32Array", Identifiers.Int32, 32},
            {"Int64Array", Identifiers.Int64, 64L},
            {"UInt16Array", Identifiers.UInt16, ushort(16)},
            {"UInt32Array", Identifiers.UInt32, uint(32)},
            {"UInt64Array", Identifiers.UInt64, ulong(64L)},
            {"FloatArray", Identifiers.Float, 3.14f},
            {"DoubleArray", Identifiers.Double, 3.14d},
            {"StringArray", Identifiers.String, "string value"},
            {"DateTimeArray", Identifiers.DateTime, DateTime.now()},
            {"GuidArray", Identifiers.Guid, UUID.randomUUID()},
            {"ByteStringArray", Identifiers.ByteString, new ByteString(new byte[]{0x01, 0x02, 0x03, 0x04})},
            {"XmlElementArray", Identifiers.XmlElement, new XmlElement("<a>hello</a>")},
            {"LocalizedTextArray", Identifiers.LocalizedText, LocalizedText.english("localized text")},
            {"QualifiedNameArray", Identifiers.QualifiedName, new QualifiedName(1234, "defg")},
            {"NodeIdArray", Identifiers.NodeId, new NodeId(1234, "abcd")}
    };

    private UaFolderNode folderNode;

    public ArrayFolder(NamespaceContext namespaceContext, UaFolderNode parentFolderNode, UUID uuid) {

        super(namespaceContext);

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(uuid),
                newQualifiedName("ArrayTypes"),
                LocalizedText.english("ArrayTypes")
        );

        getNodeManager().addNode(this.folderNode);
        parentFolderNode.addOrganizes(this.folderNode);

        addVariables();

    }

    private void addVariables() {

        for (Object[] os : STATIC_ARRAY_NODES) {
            String name = (String) os[0];
            NodeId typeId = (NodeId) os[1];
            Object value = os[2];
            Object array = Array.newInstance(value.getClass(), 5);
            for (int i = 0; i < 5; i++) {
                Array.set(array, i, value);
            }
            Variant variant = new Variant(array);

            UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                    .setNodeId(newNodeId("HelloWorld/ArrayTypes/" + name))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setUserAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName(name))
                    .setDisplayName(LocalizedText.english(name))
                    .setDataType(typeId)
                    .setTypeDefinition(Identifiers.BaseDataVariableType)
                    .setValueRank(ValueRank.OneDimension.getValue())
                    .setArrayDimensions(new UInteger[]{uint(0)})
                    .build();

            node.setValue(new DataValue(variant));

            node.getFilterChain().addLast(new AttributeLoggingFilter(AttributeId.Value::equals));

            getNodeManager().addNode(node);
            folderNode.addOrganizes(node);

        }

    }

    @Override
    public void uninstall() {

    }

}
