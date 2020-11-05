package org.eclipse.milo.examples.server.builder;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;

public class BuildInfoBuilder {

    final private BuildInfo buildInfo;

    public BuildInfoBuilder() {
        this.buildInfo = new BuildInfo(
            "urn:eclipse:milo:example-server",
            "eclipse",
            "eclipse milo example server",
            OpcUaServer.SDK_VERSION,
            "", DateTime.now()
        );
    }

    public void set(OpcUaServerConfigBuilder builder) {
        builder.setBuildInfo(buildInfo);
    }

}
