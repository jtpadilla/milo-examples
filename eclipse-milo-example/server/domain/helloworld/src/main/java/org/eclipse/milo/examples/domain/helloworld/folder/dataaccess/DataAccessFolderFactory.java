package org.eclipse.milo.examples.domain.helloworld.folder.dataaccess;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

import java.util.UUID;

public class DataAccessFolderFactory {

    final private NamespaceContext namespaceContext;
    final private UUID uuid;

    public DataAccessFolderFactory(NamespaceContext namespaceContext, UUID uuid) {
        this.namespaceContext = namespaceContext;
        this.uuid = uuid;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) {
        return new DataAccessFolder(namespaceContext, parentFolderNode, uuid);
    }

}
