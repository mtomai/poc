package it.example.crud.demo.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service(value = "vault")
public class Vault {

    private static final String    CLASSNAME = Vault.class.getSimpleName();
    private PublicKey secretKey;
    @Autowired
    private ResourceLoader resourceLoader;


    @Value("${chiave}")
    private String chiave;

    @PostConstruct
    private void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        File kFile = resourceLoader.getResource("classpath:"+chiave).getFile();
        byte[] byteBase64Key = IOUtils.toByteArray(new FileInputStream(kFile));
        byte[] byteKey = DatatypeConverter.parseBase64Binary(new String(byteBase64Key));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        secretKey = kf.generatePublic(spec);

    }

    public PublicKey getKey() {

        return secretKey;
    }

}