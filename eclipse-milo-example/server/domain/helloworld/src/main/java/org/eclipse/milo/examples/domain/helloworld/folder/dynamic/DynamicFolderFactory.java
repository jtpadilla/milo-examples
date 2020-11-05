package org.eclipse.milo.examples.domain.helloworld.folder.dynamic;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class DynamicFolderFactory {

    final private NamespaceContext namespaceContext;

    public DynamicFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new DynamicFolder(namespaceContext, parentFolderNode);
    }

}
