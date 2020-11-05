package org.eclipse.milo.examples.domain.helloworld.folder.array;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class ArrayFolderFactory {

    final private NamespaceContext namespaceContext;

    public ArrayFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new ArrayFolder(namespaceContext, parentFolderNode);
    }

}
