package org.eclipse.milo.examples.server.builder;

import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultTrustListManager;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedHttpsCertificateBuilder;
import org.eclipse.milo.opcua.stack.server.security.DefaultServerCertificateValidator;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

public class SecurityBuilder {

    final private DefaultCertificateManager certificateManager;
    final private DefaultTrustListManager trustListManager;
    final private X509Certificate httpsCertificate;
    final private X509IdentityValidator x509IdentityValidator;
    final private String applicationUri;
    final private DefaultServerCertificateValidator certificateValidator;
    final private KeyPair httpsKeyPair;
    final private UsernameIdentityValidator identityValidator;
    final private X509Certificate certificate;

    public SecurityBuilder() throws Exception {

        // Se crea el directoripo que necesitara la seguridad
        File securityTempDir = new File(System.getProperty("java.io.tmpdir"), "security");
        if (!securityTempDir.exists() && !securityTempDir.mkdirs()) {
            throw new Exception("unable to create security temp dir: " + securityTempDir);
        }
        LoggerFactory.getLogger(getClass()).info("security temp dir: {}", securityTempDir.getAbsolutePath());

        // Gestion de certificados
        KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);
        this.certificateManager = new DefaultCertificateManager(
                loader.getServerKeyPair(),
                loader.getServerCertificateChain()
        );

        File pkiDir = securityTempDir.toPath().resolve("pki").toFile();
        this.trustListManager = new DefaultTrustListManager(pkiDir);
        LoggerFactory.getLogger(getClass()).info("pki dir: {}", pkiDir.getAbsolutePath());

        this.certificateValidator = new DefaultServerCertificateValidator(trustListManager);

        // Aqui es donde se engancha tanto tiempo..
        this.httpsKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

        SelfSignedHttpsCertificateBuilder httpsCertificateBuilder = new SelfSignedHttpsCertificateBuilder(httpsKeyPair);
        httpsCertificateBuilder.setCommonName(HostnameUtil.getHostname());
        HostnameUtil.getHostnames("0.0.0.0").forEach(httpsCertificateBuilder::addDnsName);
        this.httpsCertificate = httpsCertificateBuilder.build();

        this.identityValidator = new UsernameIdentityValidator(
                true,
                authChallenge -> {
                    String username = authChallenge.getUsername();
                    String password = authChallenge.getPassword();

                    boolean userOk = Constants.USER_NAME.equals(username) && Constants.USER_PASSWORD.equals(password);
                    boolean adminOk = Constants.ADMIN_NAME.equals(username) && Constants.ADMIN_PASSWORD.equals(password);

                    return userOk || adminOk;
                }
        );

        this.x509IdentityValidator = new X509IdentityValidator(c -> true);

        // If you need to use multiple certificates you'll have to be smarter than this.
        this.certificate = certificateManager.getCertificates()
                .stream()
                .findFirst()
                .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_ConfigurationError, "no certificate found"));

        // The configured application URI must match the one in the certificate(s)
        this.applicationUri = CertificateUtil
                .getSanUri(certificate)
                .orElseThrow(() -> new UaRuntimeException(
                        StatusCodes.Bad_ConfigurationError,
                        "certificate is missing the application URI"));
    }

    public void set(OpcUaServerConfigBuilder builder) {
        builder.setApplicationUri(applicationUri);
        builder.setCertificateManager(certificateManager);
        builder.setCertificateManager(certificateManager);
        builder.setTrustListManager(trustListManager);
        builder.setCertificateValidator(certificateValidator);
        builder.setHttpsKeyPair(httpsKeyPair);
        builder.setHttpsCertificate(httpsCertificate);
        builder.setIdentityValidator(new CompositeValidator(identityValidator, x509IdentityValidator));
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

}
