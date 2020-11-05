package org.eclipse.milo.examples.domain.helloworld.method.generateevent;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class GenerateEventMethodFactory {

    final private NamespaceContext namespaceContext;

    public GenerateEventMethodFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new GenerateEventMethod(namespaceContext, parentFolderNode);
    }

}
