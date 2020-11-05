package org.eclipse.milo.examples.domain.helloworld.method.sqrt;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class SqrtMethodFactory {

    final private NamespaceContext namespaceContext;

    public SqrtMethodFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new SqrtMethod(namespaceContext, parentFolderNode);
    }

}
