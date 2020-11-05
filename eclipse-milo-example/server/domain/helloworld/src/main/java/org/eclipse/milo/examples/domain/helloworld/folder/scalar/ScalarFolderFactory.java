package org.eclipse.milo.examples.domain.helloworld.folder.scalar;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;

public class ScalarFolderFactory {

    final private NamespaceContext namespaceContext;

    public ScalarFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate(UaFolderNode parentFolderNode) throws Exception {
        return new ScalarFolder(namespaceContext, parentFolderNode);
    }

}
