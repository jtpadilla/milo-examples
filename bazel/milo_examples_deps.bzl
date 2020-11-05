load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

JACKSON_CORE_VERSION = "2.9.2"
JUNIT_VERSION = "4.12"

def milo_examples_deps():
    #
    maven_install(
        artifacts = [
            "org.apache.commons:commons-lang3:3.9",
            "com.fasterxml.jackson.core:jackson-databind:%s" % JACKSON_CORE_VERSION,
            "com.fasterxml.jackson.core:jackson-annotations:%s" % JACKSON_CORE_VERSION,
            "com.fasterxml.jackson.core:jackson-core:%s" % JACKSON_CORE_VERSION,
            "junit:junit:%s" % JUNIT_VERSION,
            "org.slf4j:slf4j-api:2.0.0-alpha1",
            "org.slf4j:slf4j-jdk14:2.0.0-alpha1",
            "org.apache.commons:commons-lang3:3.1",
            "commons-codec:commons-codec:1.9",
            "commons-logging:commons-logging:1.2",
            "org.mockito:mockito-core:3.5.13",
            "org.eclipse.milo:bsd-core:0.5.2",
            "org.eclipse.milo:bsd-generator:0.5.2",
            "org.eclipse.milo:bsd-parser:0.5.2",
            "org.eclipse.milo:bsd-parser-gson:0.5.2",
            "org.eclipse.milo:stack-client:0.5.2",
            "org.eclipse.milo:stack-core:0.5.2",
            "org.eclipse.milo:stack-server:0.5.2",
            "org.eclipse.milo:dictionary-manager:0.5.2",
            "org.eclipse.milo:dictionary-reader:0.5.2",
            "org.eclipse.milo:sdk-client:0.5.2",
            "org.eclipse.milo:sdk-core:0.5.2",
            "org.eclipse.milo:sdk-server:0.5.2",
            "org.bouncycastle:bcprov-jdk15on:1.61",
            "org.bouncycastle:bcpkix-jdk15on:1.61",
            "com.google.code.findbugs:jsr305:3.0.2",
        ],
        repositories = [
            "https://repo1.maven.org/maven2",
        ],
    )
