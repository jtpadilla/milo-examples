package org.eclipse.milo.examples.domain.helloworld.folder.scalar;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

import java.util.UUID;

public class ScalarFolderFactory {

    final private NamespaceContext namespaceContext;
    final private UUID uuid;

    public ScalarFolderFactory(NamespaceContext namespaceContext, UUID uuid) {
        this.namespaceContext = namespaceContext;
        this.uuid = uuid;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) {
        return new ScalarFolder(namespaceContext, parentFolderNode, uuid);
    }

}
