/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client.readwritecustomdatatypenode;

import org.eclipse.milo.examples.client.util.ClientExample;
import org.eclipse.milo.examples.client.util.ClientExampleRunner;
import org.eclipse.milo.examples.common.types.CustomStructType;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class ReadWriteCustomDataTypeNodeExample implements ClientExample {

    public static void main(String[] args) throws Exception {
        ReadWriteCustomDataTypeNodeExample example = new ReadWriteCustomDataTypeNodeExample();

        new ClientExampleRunner(example).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();

        registerCustomCodec(client);

        // synchronous read request via VariableNode
        UaVariableNode node = client.getAddressSpace().getVariableNode(
            new NodeId(2, "HelloWorld/CustomStructTypeVariable")
        );

        logger.info("DataType={}", node.getDataType());

        // Read the current value
        DataValue value = node.readValue();
        logger.info("Value={}", value);

        Variant variant = value.getValue();
        ExtensionObject xo = (ExtensionObject) variant.getValue();

        CustomStructType decoded = (CustomStructType) xo.decode(
            client.getSerializationContext()
        );
        logger.info("Decoded={}", decoded);

        // Write a modified value
        CustomStructType modified = new CustomStructType(
            decoded.getFoo() + "bar",
            uint(decoded.getBar().intValue() + 1),
            !decoded.isBaz()
        );
        ExtensionObject modifiedXo = ExtensionObject.encode(
            client.getSerializationContext(),
            modified,
            xo.getEncodingId(),
            OpcUaDefaultBinaryEncoding.getInstance()
        );

        node.writeValue(new DataValue(new Variant(modifiedXo)));

        // Read the modified value back
        value = node.readValue();
        logger.info("Value={}", value);

        variant = value.getValue();
        xo = (ExtensionObject) variant.getValue();

        decoded = (CustomStructType) xo.decode(
            client.getSerializationContext()
        );
        logger.info("Decoded={}", decoded);

        future.complete(client);
    }

    private void registerCustomCodec(OpcUaClient client) {
        NodeId binaryEncodingId = CustomStructType.BINARY_ENCODING_ID
            .local(client.getNamespaceTable())
            .orElseThrow(() -> new IllegalStateException("namespace not found"));

        // Register codec with the client DataTypeManager instance
        client.getDataTypeManager().registerCodec(
            binaryEncodingId,
            new CustomStructType.Codec().asBinaryCodec()
        );
    }

}
