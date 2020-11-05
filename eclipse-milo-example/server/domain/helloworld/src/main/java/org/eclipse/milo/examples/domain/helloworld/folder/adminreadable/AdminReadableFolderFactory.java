package org.eclipse.milo.examples.domain.helloworld.folder.adminreadable;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class AdminReadableFolderFactory {

    final private NamespaceContext namespaceContext;

    public AdminReadableFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new AdminReadableFolder(namespaceContext, parentFolderNode);
    }

}
