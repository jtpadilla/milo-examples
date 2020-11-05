package org.eclipse.milo.examples.domain.helloworld.folder.adminwritable;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class AdminWritableFolderFactory {

    final private NamespaceContext namespaceContext;

    public AdminWritableFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new AdminWritableFolder(namespaceContext, parentFolderNode);
    }

}
