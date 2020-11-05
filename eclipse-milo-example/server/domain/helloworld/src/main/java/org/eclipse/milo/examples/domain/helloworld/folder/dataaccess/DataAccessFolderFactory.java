package org.eclipse.milo.examples.domain.helloworld.folder.dataaccess;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class DataAccessFolderFactory {

    final private NamespaceContext namespaceContext;

    public DataAccessFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new DataAccessFolder(namespaceContext, parentFolderNode);
    }

}
