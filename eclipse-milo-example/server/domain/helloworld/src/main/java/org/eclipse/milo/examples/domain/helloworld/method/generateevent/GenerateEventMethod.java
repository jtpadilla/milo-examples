package org.eclipse.milo.examples.domain.helloworld.method.generateevent;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

import java.util.UUID;

public class GenerateEventMethod extends AbstractNodeDomainCloseable {

    final private UaMethodNode methodNode;

    public GenerateEventMethod(NamespaceContext namespaceContext, UaFolderNode parentFolderNode, UUID uuid) {

        super(namespaceContext);

        this.methodNode = UaMethodNode.builder(getNodeContext())
                .setNodeId(newNodeId(uuid))
                .setBrowseName(newQualifiedName("generateEvent(eventTypeId)"))
                .setDisplayName(new LocalizedText(null, "generateEvent(eventTypeId)"))
                .setDescription(LocalizedText.english("Generate an Event with the TypeDefinition indicated by eventTypeId."))
                .build();

        GenerateEventMethodHandler generateEventMethodHandler = new GenerateEventMethodHandler(methodNode);
        methodNode.setInputArguments(generateEventMethodHandler.getInputArguments());
        methodNode.setOutputArguments(generateEventMethodHandler.getOutputArguments());
        methodNode.setInvocationHandler(generateEventMethodHandler);

        getNodeManager().addNode(methodNode);

        methodNode.addReference(new Reference(
                methodNode.getNodeId(),
                Identifiers.HasComponent,
                parentFolderNode.getNodeId().expanded(),
                false
        ));

    }

    @Override
    public void uninstall() {

    }

}
