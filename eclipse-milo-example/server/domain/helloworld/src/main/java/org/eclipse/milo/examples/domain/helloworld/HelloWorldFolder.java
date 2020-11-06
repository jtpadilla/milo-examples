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
import org.eclipse.milo.examples.domain.helloworld.type.customstruct.CustomStructTypeFactory;
import org.eclipse.milo.examples.domain.helloworld.type.customunion.CustomUnionTypeFactory;
import org.eclipse.milo.examples.domain.helloworld.type.custonenum.CustomEnumTypeFactory;
import org.eclipse.milo.examples.util.AbstractNodeDomainCloseable;
import org.eclipse.milo.examples.util.DomainCloseable;
import org.eclipse.milo.examples.util.NamespaceContext;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldFolder extends AbstractNodeDomainCloseable {

    final private Logger logger = LoggerFactory.getLogger(getClass());

    final private UaFolderNode folderNode;

    // Factory
    final private CustomEnumTypeFactory customEnumTypeFactory;
    final private CustomStructTypeFactory customStructTypeFactory;
    final private CustomUnionTypeFactory customUnionTypeFactory;

    final private AdminReadableFolderFactory adminReadableFolderFactory;
    final private AdminWritableFolderFactory adminWritableFolderFactory;
    final private ArrayFolderFactory arrayFolderFactory;
    final private DataAccessFolderFactory dataAccessFolderFactory;
    final private DynamicFolderFactory dynamicFolderFactory;
    final private GenerateEventMethodFactory generateEventMethodNodeFactory;
    final private ScalarFolderFactory scalarFolderFactory;
    final private SqrtMethodFactory sqrtMethodFactory;
    final private WriteOnlyFolderFactory writeOnlyFolderFactory;

    // Instances
    private DomainCloseable customEnumTypeInstance;
    private DomainCloseable customStructTypeInstance;
    private DomainCloseable customUnionTypeInstance;

    private DomainCloseable adminReadableNodesFolder;
    private DomainCloseable adminWritableNodesFolder;
    private DomainCloseable arrayFolder;
    private DomainCloseable dataAccessNodesFolder;
    private DomainCloseable dynamicFolder;
    private DomainCloseable generateEventMethodNode;
    private DomainCloseable scalarFolder;
    private DomainCloseable sqrtMethod;
    private DomainCloseable writeOnlyFolder;

    HelloWorldFolder(NamespaceContext namespaceContext) {

        super(namespaceContext);

        customEnumTypeFactory = new CustomEnumTypeFactory(namespaceContext);
        customStructTypeFactory = new CustomStructTypeFactory(namespaceContext);
        customUnionTypeFactory = new CustomUnionTypeFactory(namespaceContext);

        adminReadableFolderFactory = new AdminReadableFolderFactory(namespaceContext);
        adminWritableFolderFactory = new AdminWritableFolderFactory(namespaceContext);
        arrayFolderFactory = new ArrayFolderFactory(namespaceContext);
        dataAccessFolderFactory = new DataAccessFolderFactory(namespaceContext);
        dynamicFolderFactory = new DynamicFolderFactory(namespaceContext);
        generateEventMethodNodeFactory = new GenerateEventMethodFactory(namespaceContext);
        scalarFolderFactory = new ScalarFolderFactory(namespaceContext);
        sqrtMethodFactory = new SqrtMethodFactory(namespaceContext);
        writeOnlyFolderFactory = new WriteOnlyFolderFactory(namespaceContext);

        this.folderNode = new UaFolderNode(
                getNodeContext(),
                newNodeId("HelloWorld"),
                newQualifiedName("HelloWorld"),
                LocalizedText.english("HelloWorld")
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

    public void addChilds() {

        try {

            this.customEnumTypeInstance = customEnumTypeFactory.instantiate(folderNode);
            this.customStructTypeInstance = customStructTypeFactory.instantiate(folderNode);
            this.customUnionTypeInstance = customUnionTypeFactory.instantiate(folderNode);

            this.adminReadableNodesFolder = adminReadableFolderFactory.instantiate(folderNode);
            this.adminWritableNodesFolder = adminWritableFolderFactory.instantiate(folderNode);
            this.arrayFolder = arrayFolderFactory.instantiate(folderNode);
            this.dataAccessNodesFolder = dataAccessFolderFactory.instantiate(folderNode);
            this.dynamicFolder = dynamicFolderFactory.instantiate(folderNode);
            this.generateEventMethodNode = generateEventMethodNodeFactory.instantiate(folderNode);
            this.scalarFolder = scalarFolderFactory.instantiate(folderNode);
            this.sqrtMethod = sqrtMethodFactory.instantiate(folderNode);
            this.writeOnlyFolder = writeOnlyFolderFactory.instantiate(folderNode);

        } catch (Exception e) {
            logger.error("Ha sido imposible ensamblar el nodo", e);
        }

    }

}
