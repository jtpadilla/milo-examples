package org.eclipse.milo.examples.domain.helloworld.method.sqrt;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class SqrtMethod extends AbstractNodeDomainCloseable {

    final private UaMethodNode methodNode;

    public SqrtMethod(NamespaceContext namespaceContext, UaFolderNode parentFolderNode) {

        super(namespaceContext);

        this.methodNode = UaMethodNode.builder(getNodeContext())
                .setNodeId(newNodeId("HelloWorld/sqrt(x)"))
                .setBrowseName(newQualifiedName("sqrt(x)"))
                .setDisplayName(new LocalizedText(null, "sqrt(x)"))
                .setDescription(
                        LocalizedText.english("Returns the correctly rounded positive square root of a double value."))
                .build();

        SqrtMethodHandler sqrtMethod = new SqrtMethodHandler(methodNode);
        methodNode.setInputArguments(sqrtMethod.getInputArguments());
        methodNode.setOutputArguments(sqrtMethod.getOutputArguments());
        methodNode.setInvocationHandler(sqrtMethod);

        getNodeManager().addNode(methodNode);

        methodNode.addReference(new Reference(
                methodNode.getNodeId(),
                Identifiers.HasComponent,
                parentFolderNode.getNodeId().expanded(),
                false
        ));

    }

}
