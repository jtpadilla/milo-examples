package org.eclipse.milo.examples.domain.helloworld;

import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.examples.util.NodeIdMostPartGenerator;

public class HelloWorldFolderFactory {

    final private NamespaceContext namespaceContext;
    final private NodeIdMostPartGenerator nodeIdGenerator;

    public HelloWorldFolderFactory(NamespaceContext namespaceContext, NodeIdMostPartGenerator nodeIdGenerator) {
        this.namespaceContext = namespaceContext;
        this.nodeIdGenerator = nodeIdGenerator;
    }

    public DomainCloseable instantiate(String relativeName) {
        return new HelloWorldFolder(namespaceContext, relativeName, nodeIdGenerator.getNext());
    }

}
