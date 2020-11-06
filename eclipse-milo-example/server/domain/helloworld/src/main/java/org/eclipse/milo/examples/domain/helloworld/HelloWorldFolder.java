package org.eclipse.milo.examples.domain.helloworld;

import org.eclipse.milo.examples.domain.helloworld.folder.adminreadable.AdminReadableFolder;
import org.eclipse.milo.examples.domain.helloworld.folder.adminwritable.AdminWritableFolder;
import org.eclipse.milo.examples.domain.helloworld.folder.array.ArrayFolder;
import org.eclipse.milo.examples.domain.helloworld.folder.dataaccess.DataAccessFolder;
import org.eclipse.milo.examples.domain.helloworld.folder.dynamic.DynamicFolder;
import org.eclipse.milo.examples.domain.helloworld.folder.scalar.ScalarFolder;
import org.eclipse.milo.examples.domain.helloworld.folder.writeonly.WriteOnlyFolder;
import org.eclipse.milo.examples.domain.helloworld.method.generateevent.GenerateEventMethod;
import org.eclipse.milo.examples.domain.helloworld.method.sqrt.SqrtMethod;
import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HelloWorldFolder extends AbstractNodeDomainCloseable {

    static final long ADMIN_READABLE_FOLDER_LEAST = 0x0000;
    static final long ADMIN_WRITABLE_FOLDER_LEAST = 0x0001;
    static final long ARRAY_FOLDER_LEAST = 0x0002;
    static final long DATA_ACCESS_FOLDER_LEAST = 0x0003;
    static final long DYNAMIC_FOLDER_LEAST = 0x0004;
    static final long SCALAR_FOLDER_LEAST = 0x0005;
    static final long WRITE_ONLY_FOLDER_LEAST = 0x0006;

    static final long SQRT_METHOD_LEAST = 0x1000;
    static final long GENERATE_EVENT_METHOD_LEAST = 0x1001;

    static public DomainCloseable instantiate(NamespaceContext namespaceContext, String canonicalName, long globalId) {
        return new HelloWorldFolder(namespaceContext, canonicalName, globalId);
    }

    final private long globalId;
    final private UaFolderNode folderNode;
    final private List<DomainCloseable> closeableNodes = new ArrayList<>();

    HelloWorldFolder(NamespaceContext namespaceContext, String canonicalName, long globalId) {

        super(namespaceContext);

        this.globalId = globalId;

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(canonicalName),
                newQualifiedName(canonicalName),
                LocalizedText.english(canonicalName)
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

    private void addChilds() {

        closeableNodes.add(AdminReadableFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, ADMIN_READABLE_FOLDER_LEAST)));
        closeableNodes.add(AdminWritableFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, ADMIN_WRITABLE_FOLDER_LEAST)));
        closeableNodes.add(ArrayFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, ARRAY_FOLDER_LEAST)));
        closeableNodes.add(DataAccessFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, DATA_ACCESS_FOLDER_LEAST)));
        closeableNodes.add(DynamicFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, DYNAMIC_FOLDER_LEAST)));
        closeableNodes.add(ScalarFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, SCALAR_FOLDER_LEAST)));
        closeableNodes.add(WriteOnlyFolder.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, WRITE_ONLY_FOLDER_LEAST)));

        closeableNodes.add(SqrtMethod.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, SQRT_METHOD_LEAST)));
        closeableNodes.add(GenerateEventMethod.instantiate(getNamespaceContext(), folderNode, new UUID(globalId, GENERATE_EVENT_METHOD_LEAST)));

    }

    @Override
    public void uninstall() {

        // Primero se desinstalan los hijos
        for (DomainCloseable domainCloseable : closeableNodes) {
            domainCloseable.uninstall();
        }

        // Despues el padre
        getNodeManager().removeNode(folderNode);

    }

}
