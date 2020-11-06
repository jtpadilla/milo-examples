package org.eclipse.milo.examples.domain.helloworld;

import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.examples.util.NodeIdMostPartGenerator;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class HelloWorldContainer extends AbstractNodeDomainCloseable {

    final private NodeIdMostPartGenerator nodeIdGenerator;
    final private HelloWorldFolderFactory factory;

    final private UaFolderNode folderNode;

    public HelloWorldContainer(NamespaceContext namespaceContext) {

        super(namespaceContext);

        this.nodeIdGenerator = new NodeIdMostPartGenerator(FOLDER_CLASS_ID);
        this.factory = new HelloWorldFolderFactory(getNamespaceContext(), nodeIdGenerator);

        String folderName = String.format("HelloWorld");

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(folderName),
                newQualifiedName(folderName),
                LocalizedText.english(folderName)
        );

        // y se incorpora el NodeManager
        getNodeManager().addNode(folderNode);

        // La nueva carpeta se mostrara dentro de la carpeta 'Objects' del servidor.
        folderNode.addReference(
                new Reference(
                        this.folderNode.getNodeId(),
                        Identifiers.Organizes,
                        Identifiers.ObjectsFolder.expanded(),
                        false
                )
        );

        addChilds();

    }

    DomainCloseable f1;
    DomainCloseable f2;
    DomainCloseable f3;

    public void addChilds() {
        f1 = factory.instantiate(1);
        f2 = factory.instantiate(2);
        f3 = factory.instantiate(3);
    }

    @Override
    public void uninstall() {

    }

}
