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

    static final private String FOLDER_NAME = "HelloWorld";

    static public DomainCloseable instantiate(NamespaceContext namespaceContext) {
        return new HelloWorldContainer(namespaceContext);
    }

    final private UaFolderNode folderNode;

    public HelloWorldContainer(NamespaceContext namespaceContext) {

        super(namespaceContext);

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(FOLDER_NAME),
                newQualifiedName(FOLDER_NAME),
                LocalizedText.english(FOLDER_NAME)
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
        f1 = HelloWorldFolder.instantiate(getNamespaceContext(), folderNode, FOLDER_NAME + "1", NodeIdMostPartGenerator.getInstance().getNext());
        f2 = HelloWorldFolder.instantiate(getNamespaceContext(), folderNode, FOLDER_NAME + "2", NodeIdMostPartGenerator.getInstance().getNext());
        f3 = HelloWorldFolder.instantiate(getNamespaceContext(), folderNode, FOLDER_NAME + "3", NodeIdMostPartGenerator.getInstance().getNext());
    }

    @Override
    public void uninstall() {
        f1.uninstall();
        f2.uninstall();
        f3.uninstall();
    }

}
