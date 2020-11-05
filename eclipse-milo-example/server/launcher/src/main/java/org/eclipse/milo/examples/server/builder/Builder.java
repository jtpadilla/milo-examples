package org.eclipse.milo.examples.server.builder;

import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public class Builder {

    static public OpcUaServerConfig buildConfig() throws Exception {

        SecurityBuilder securityBuilder = new SecurityBuilder();
        EndpointBuilder endpointBuilder = new EndpointBuilder(securityBuilder.getCertificate());
        BuildInfoBuilder buildInfoBuilder = new BuildInfoBuilder();

        OpcUaServerConfigBuilder serverConfigBuilder = OpcUaServerConfig.builder();

        serverConfigBuilder.setApplicationName(LocalizedText.english(Constants.APPLICATION_NAME));
        serverConfigBuilder.setProductUri(Constants.PUBLIC_URI);
        endpointBuilder.set(serverConfigBuilder);
        buildInfoBuilder.set(serverConfigBuilder);
        securityBuilder.set(serverConfigBuilder);

        return serverConfigBuilder.build();

    }


}
