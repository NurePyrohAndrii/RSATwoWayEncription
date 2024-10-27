package com.rsaserver.RSA.encryption.RSA;

import lombok.Getter;

@Getter
public class RSAKey {

    private static final String privateKeyHeader = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String privateKeyFooter = "-----END RSA PRIVATE KEY-----";

    public static final String publicKeyHeader = "-----BEGIN PUBLIC KEY-----";
    public static final String publicKeyFooter = "-----END PUBLIC KEY-----";

    private final String key;
    private final RSAKeyType keyType;

    public RSAKey(String base64KeyBody, RSAKeyType keyType) {
        String header;
        String footer = switch (keyType) {
            case PRIVATE -> {
                header = privateKeyHeader;
                yield privateKeyFooter;
            }
            case PUBLIC -> {
                header = publicKeyHeader;
                yield publicKeyFooter;
            }
        };
        this.key = header + base64KeyBody + footer;
        this.keyType = keyType;
    }

}
