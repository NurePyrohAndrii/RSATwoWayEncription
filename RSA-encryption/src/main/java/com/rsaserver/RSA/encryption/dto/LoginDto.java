package com.rsaserver.RSA.encryption.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    private String base64EncodedEncryptedData;
    private String rsaPublicKey;
}
