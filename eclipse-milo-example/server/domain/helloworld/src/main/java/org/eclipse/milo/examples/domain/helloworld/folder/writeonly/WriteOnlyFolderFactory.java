package org.eclipse.milo.examples.domain.helloworld.folder.writeonly;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class WriteOnlyFolderFactory {

    final private NamespaceContext namespaceContext;

    public WriteOnlyFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new WriteOnlyFolder(namespaceContext, parentFolderNode);
    }

}
