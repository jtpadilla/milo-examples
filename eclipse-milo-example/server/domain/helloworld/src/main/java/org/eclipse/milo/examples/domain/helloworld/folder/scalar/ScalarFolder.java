package org.eclipse.milo.examples.domain.helloworld.folder.scalar;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.AttributeLoggingFilter;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;

import java.util.UUID;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.*;

public class ScalarFolder extends AbstractNodeDomainCloseable {

    private static final Object[][] STATIC_SCALAR_NODES = new Object[][]{
            {"Boolean", Identifiers.Boolean, new Variant(false)},
            {"Byte", Identifiers.Byte, new Variant(ubyte(0x00))},
            {"SByte", Identifiers.SByte, new Variant((byte) 0x00)},
            {"Integer", Identifiers.Integer, new Variant(32)},
            {"Int16", Identifiers.Int16, new Variant((short) 16)},
            {"Int32", Identifiers.Int32, new Variant(32)},
            {"Int64", Identifiers.Int64, new Variant(64L)},
            {"UInteger", Identifiers.UInteger, new Variant(uint(32))},
            {"UInt16", Identifiers.UInt16, new Variant(ushort(16))},
            {"UInt32", Identifiers.UInt32, new Variant(uint(32))},
            {"UInt64", Identifiers.UInt64, new Variant(ulong(64L))},
            {"Float", Identifiers.Float, new Variant(3.14f)},
            {"Double", Identifiers.Double, new Variant(3.14d)},
            {"String", Identifiers.String, new Variant("string value")},
            {"DateTime", Identifiers.DateTime, new Variant(DateTime.now())},
            {"Guid", Identifiers.Guid, new Variant(UUID.randomUUID())},
            {"ByteString", Identifiers.ByteString, new Variant(new ByteString(new byte[]{0x01, 0x02, 0x03, 0x04}))},
            {"XmlElement", Identifiers.XmlElement, new Variant(new XmlElement("<a>hello</a>"))},
            {"LocalizedText", Identifiers.LocalizedText, new Variant(LocalizedText.english("localized text"))},
            {"QualifiedName", Identifiers.QualifiedName, new Variant(new QualifiedName(1234, "defg"))},
            {"NodeId", Identifiers.NodeId, new Variant(new NodeId(1234, "abcd"))},
            {"Variant", Identifiers.BaseDataType, new Variant(32)},
            {"Duration", Identifiers.Duration, new Variant(1.0)},
            {"UtcTime", Identifiers.UtcTime, new Variant(DateTime.now())},
    };

    private UaFolderNode folderNode;

    public ScalarFolder(NamespaceContext namespaceContext, UaFolderNode parentFolderNode, UUID uuid) {

        super(namespaceContext);

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(uuid),
                newQualifiedName("ScalarTypes"),
                LocalizedText.english("ScalarTypes")
        );

        getNodeManager().addNode(this.folderNode);
        parentFolderNode.addOrganizes(this.folderNode);

        addVariables();

    }

    private void addVariables() {

        for (Object[] os : STATIC_SCALAR_NODES) {
            String name = (String) os[0];
            NodeId typeId = (NodeId) os[1];
            Variant variant = (Variant) os[2];

            UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                    .setNodeId(newNodeId("HelloWorld/ScalarTypes/" + name))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setUserAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName(name))
                    .setDisplayName(LocalizedText.english(name))
                    .setDataType(typeId)
                    .setTypeDefinition(Identifiers.BaseDataVariableType)
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
