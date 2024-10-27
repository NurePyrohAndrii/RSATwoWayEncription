package com.rsaserver.RSA.encryption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsaserver.RSA.encryption.RSA.RSAKeyType;
import com.rsaserver.RSA.encryption.dto.InitSecureConnectionResponse;
import com.rsaserver.RSA.encryption.data.LoginCredentials;
import com.rsaserver.RSA.encryption.dto.LoginDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static com.rsaserver.RSA.encryption.RSA.RSAUtils.decrypt;
import static com.rsaserver.RSA.encryption.RSA.RSAUtils.encrypt;
import static com.rsaserver.RSA.encryption.RSA.RSAUtils.getKey;
import static com.rsaserver.RSA.encryption.RSA.RSAUtils.getPublicKeyFromPem;

@Service
public class LoginService {

    private final KeyPair rsaKeyPair;

    public LoginService() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        rsaKeyPair = generator.generateKeyPair();
    }

    public InitSecureConnectionResponse initSecureConnection() {
        InitSecureConnectionResponse dto = new InitSecureConnectionResponse();
        String publicKey = getKey(rsaKeyPair.getPublic(), RSAKeyType.PUBLIC);
        dto.setPublicKey(publicKey);
        System.out.println(publicKey);
        System.out.println(getKey(rsaKeyPair.getPrivate(), RSAKeyType.PRIVATE));
        return dto;
    }

    @SneakyThrows
    public LoginDto login(LoginDto loginDto) {
        String encryptedData = loginDto.getBase64EncodedEncryptedData();
        String publicKey = loginDto.getRsaPublicKey();

        String decryptedData = decrypt(encryptedData, rsaKeyPair.getPrivate());
        ObjectMapper objectMapper = new ObjectMapper();
        LoginCredentials credentials = objectMapper.readValue(decryptedData, LoginCredentials.class);
        String messageToEncrypt = String.format("Login successful for user %s", credentials.getLogin());

        PublicKey publicKeyObject =  getPublicKeyFromPem(publicKey);
        String encryptedMessage = encrypt(messageToEncrypt, publicKeyObject);

        return LoginDto.builder()
                .base64EncodedEncryptedData(encryptedMessage)
                .build();
    }
}
