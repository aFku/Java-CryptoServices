package org.rcbg.afku.CryptoGenerator.k8sClient;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCRD;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCRD;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class K8sCrdClientFactory {

    public static MixedOperation<PasswordProfileCRD, KubernetesResourceList<PasswordProfileCRD>, Resource<PasswordProfileCRD>> getPasswordProfileClient(){
        try(KubernetesClient client = new KubernetesClientBuilder().build()) {
            return client.resources(PasswordProfileCRD.class);
        }
    }

    public static MixedOperation<AsymmetricKeysProfileCRD, KubernetesResourceList<AsymmetricKeysProfileCRD>, Resource<AsymmetricKeysProfileCRD>> getAsymmetricKeysProfileClient(){
        try(KubernetesClient client = new KubernetesClientBuilder().build()) {
            return client.resources(AsymmetricKeysProfileCRD.class);
        }
    }

}