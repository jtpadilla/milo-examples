package org.eclipse.milo.examples.domain.helloworld;

import org.eclipse.milo.examples.domain.helloworld.folder.adminreadable.AdminReadableFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.folder.adminwritable.AdminWritableFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.folder.array.ArrayFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.folder.dataaccess.DataAccessFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.folder.dynamic.DynamicFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.folder.scalar.ScalarFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.folder.writeonly.WriteOnlyFolderFactory;
import org.eclipse.milo.examples.domain.helloworld.method.generateevent.GenerateEventMethodFactory;
import org.eclipse.milo.examples.domain.helloworld.method.sqrt.SqrtMethodFactory;
import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    final private Logger logger = LoggerFactory.getLogger(getClass());

    final private UaFolderNode folderNode;

    final private AdminReadableFolderFactory adminReadableFolderFactory;
    final private AdminWritableFolderFactory adminWritableFolderFactory;
    final private ArrayFolderFactory arrayFolderFactory;
    final private DataAccessFolderFactory dataAccessFolderFactory;
    final private DynamicFolderFactory dynamicFolderFactory;
    final private ScalarFolderFactory scalarFolderFactory;
    final private WriteOnlyFolderFactory writeOnlyFolderFactory;

    final private SqrtMethodFactory sqrtMethodFactory;
    final private GenerateEventMethodFactory generateEventMethodNodeFactory;

    private List<DomainCloseable> closeableNodes;

    HelloWorldFolder(NamespaceContext namespaceContext, String relativeName, long globalId) {

        super(namespaceContext);
        this.closeableNodes = new ArrayList<>();

        adminReadableFolderFactory = new AdminReadableFolderFactory(namespaceContext, new UUID(globalId, ADMIN_READABLE_FOLDER_LEAST));
        adminWritableFolderFactory = new AdminWritableFolderFactory(namespaceContext, new UUID(globalId, ADMIN_WRITABLE_FOLDER_LEAST));
        arrayFolderFactory = new ArrayFolderFactory(namespaceContext, new UUID(globalId, ARRAY_FOLDER_LEAST));
        dataAccessFolderFactory = new DataAccessFolderFactory(namespaceContext, new UUID(globalId, DATA_ACCESS_FOLDER_LEAST));
        dynamicFolderFactory = new DynamicFolderFactory(namespaceContext, new UUID(globalId, DYNAMIC_FOLDER_LEAST));
        generateEventMethodNodeFactory = new GenerateEventMethodFactory(namespaceContext, new UUID(globalId, GENERATE_EVENT_METHOD_LEAST));
        scalarFolderFactory = new ScalarFolderFactory(namespaceContext, new UUID(globalId, SCALAR_FOLDER_LEAST));
        sqrtMethodFactory = new SqrtMethodFactory(namespaceContext, new UUID(globalId, SQRT_METHOD_LEAST));
        writeOnlyFolderFactory = new WriteOnlyFolderFactory(namespaceContext, new UUID(globalId, WRITE_ONLY_FOLDER_LEAST));

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId(relativeName),
                newQualifiedName(relativeName),
                LocalizedText.english(relativeName)
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
        closeableNodes.add(adminReadableFolderFactory.instantiate(folderNode));
        closeableNodes.add(adminWritableFolderFactory.instantiate(folderNode));
        closeableNodes.add(arrayFolderFactory.instantiate(folderNode));
        closeableNodes.add(dataAccessFolderFactory.instantiate(folderNode));
        closeableNodes.add(dynamicFolderFactory.instantiate(folderNode));
        closeableNodes.add(generateEventMethodNodeFactory.instantiate(folderNode));
        closeableNodes.add(scalarFolderFactory.instantiate(folderNode));
        closeableNodes.add(sqrtMethodFactory.instantiate(folderNode));
        closeableNodes.add(writeOnlyFolderFactory.instantiate(folderNode));
    }

    @Override
    public void uninstall() {

        // Primero se desinstalan los hijos
        for (DomainCloseable domainCloseable : closeableNodes) {
            domainCloseable.uninstall();
        }

        // Despues el padre
        // TODO:...

    }

}
