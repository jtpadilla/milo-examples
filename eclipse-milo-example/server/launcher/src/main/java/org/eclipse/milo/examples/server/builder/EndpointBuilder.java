package org.eclipse.milo.examples.server.builder;

import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;

import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.*;

public class EndpointBuilder {

    final private Set<EndpointConfiguration> endpointConfigurations;

    public EndpointBuilder(X509Certificate certificate) {

        this.endpointConfigurations =  new LinkedHashSet<>();

        // Lista de IPs por las que se levantara el servidor TCP
        List<String> bindAddresses = newArrayList();
        bindAddresses.add("0.0.0.0");

        // Lista de hostnames
        Set<String> hostnames = new LinkedHashSet<>();
        hostnames.add(HostnameUtil.getHostname());
        hostnames.addAll(HostnameUtil.getHostnames("0.0.0.0"));

        for (String bindAddress : bindAddresses) {

            for (String hostname : hostnames) {

                // Se combinan todas las deirecciones de bindings con todos los
                // hostnames registrados
                EndpointConfiguration.Builder builder = EndpointConfiguration.newBuilder()
                        .setBindAddress(bindAddress)
                        .setHostname(hostname)
                        .setPath(Constants.ENDPOINT_PATH)
                        .setCertificate(certificate)
                        .addTokenPolicies(
                                USER_TOKEN_POLICY_ANONYMOUS,
                                USER_TOKEN_POLICY_USERNAME,
                                USER_TOKEN_POLICY_X509);


                EndpointConfiguration.Builder noSecurityBuilder = builder.copy()
                        .setSecurityPolicy(SecurityPolicy.None)
                        .setSecurityMode(MessageSecurityMode.None);

                endpointConfigurations.add(buildTcpEndpoint(noSecurityBuilder));
                endpointConfigurations.add(buildHttpsEndpoint(noSecurityBuilder));

                // TCP Basic256Sha256 / SignAndEncrypt
                endpointConfigurations.add(
                        buildTcpEndpoint(builder.copy()
                                .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                                .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                        )
                );

                // HTTPS Basic256Sha256 / Sign (SignAndEncrypt not allowed for HTTPS)
                endpointConfigurations.add(
                        buildHttpsEndpoint(builder.copy()
                                .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                                .setSecurityMode(MessageSecurityMode.Sign)
                        )
                );

                /*
                 * It's good practice to provide a discovery-specific endpoint with no security.
                 * It's required practice if all regular endpoints have security configured.
                 *
                 * Usage of the  "/discovery" suffix is defined by OPC UA Part 6:
                 *
                 * Each OPC UA Server Application implements the Discovery Service Set. If the OPC UA Server requires a
                 * different address for this Endpoint it shall create the address by appending the path "/discovery" to
                 * its base address.
                 */

                EndpointConfiguration.Builder discoveryBuilder = builder.copy()
                        .setPath(Constants.ENDPOINT_DISCOVERY_PATH)
                        .setSecurityPolicy(SecurityPolicy.None)
                        .setSecurityMode(MessageSecurityMode.None);

                endpointConfigurations.add(buildTcpEndpoint(discoveryBuilder));
                endpointConfigurations.add(buildHttpsEndpoint(discoveryBuilder));

            }
        }

    }

    private static EndpointConfiguration buildTcpEndpoint(EndpointConfiguration.Builder base) {
        return base.copy()
                .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
                .setBindPort(Constants.TCP_BIND_PORT)
                .build();
    }

    private static EndpointConfiguration buildHttpsEndpoint(EndpointConfiguration.Builder base) {
        return base.copy()
                .setTransportProfile(TransportProfile.HTTPS_UABINARY)
                .setBindPort(Constants.HTTPS_BIND_PORT)
                .build();
    }

    public void set(OpcUaServerConfigBuilder builder) {
        builder.setEndpoints(endpointConfigurations);
    }

}
