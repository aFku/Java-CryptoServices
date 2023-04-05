package org.rcbg.afku.CryptoGenerator.controllers;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCRD;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;

@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping
    public String test(){

        try(KubernetesClient client = new KubernetesClientBuilder().build()) {
            return client.resources(PasswordProfileCRD.class).inNamespace("default").list().getMetadata().toString();
        }
    }
}
