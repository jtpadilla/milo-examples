package org.eclipse.milo.examples.domain.helloworld;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;

public class HelloWorldFolderFactory {

    final private NamespaceContext namespaceContext;

    public HelloWorldFolderFactory(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public DomainCloseable instantiate() throws Exception {
        return new HelloWorldFolder(namespaceContext);
    }

}
