/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client.browse;

import org.eclipse.milo.examples.client.util.ClientExample;
import org.eclipse.milo.examples.client.util.ClientExampleRunner;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

public class BrowseExample implements ClientExample {

    public static void main(String[] args) {
        new ClientExampleRunner(new BrowseExample()).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {

        // Conexion sincrona
        client.connect().get();

        // Se empieza en la carpeta raiz
        browseNode("", client, Identifiers.RootFolder);

        future.complete(client);

    }

    private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {

        BrowseDescription browse = new BrowseDescription(
            browseRoot,
            BrowseDirection.Forward,
            Identifiers.References,
            true,
            uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
            uint(BrowseResultMask.All.getValue())
        );

        try {

            BrowseResult browseResult = client.browse(browse).get();

            List<ReferenceDescription> references = toList(browseResult.getReferences());

            for (ReferenceDescription rd : references) {

                // Se muetsran los de la carpeta actual
                System.out.println(
                    String.format("%s %s", indent, referenceDescriptionToString(rd))
                );

                // Se muestran los hijos
//                ExpandedNodeId nodeId = rd.getNodeId();
//                Optional<NodeId> local = nodeId.local(client.getNamespaceTable());
//                if (local.isPresent()) {
//                    browseNode(indent + "  ", client, nodeId);
//                }

                rd.getNodeId().local(client.getNamespaceTable())
                    .ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
        }
    }

    private String referenceDescriptionToString(ReferenceDescription referenceDescription) {

        QualifiedName browseName = referenceDescription.getBrowseName();
        LocalizedText displayName = referenceDescription.getDisplayName();
        ExpandedNodeId nodeId = referenceDescription.getNodeId();

        return String.format("browseName=%s, displayName[%s]=%s, nodeId=%s",
                browseName.getName(),
                displayName.getLocale(),
                displayName.getText(),
                nodeId.toString()
        );

    }

}

