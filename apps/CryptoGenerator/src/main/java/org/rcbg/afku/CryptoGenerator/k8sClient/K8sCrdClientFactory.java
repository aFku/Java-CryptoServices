package org.rcbg.afku.CryptoGenerator.k8sClient;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCRD;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCRD;

public class K8sCrdClientFactory {

    private final static KubernetesClient client;

    static{
        client = new KubernetesClientBuilder().build();
    }

    public static MixedOperation<PasswordProfileCRD, KubernetesResourceList<PasswordProfileCRD>, Resource<PasswordProfileCRD>> getPasswordProfileClient(){
        return client.resources(PasswordProfileCRD.class);
    }

    public static MixedOperation<AsymmetricKeysProfileCRD, KubernetesResourceList<AsymmetricKeysProfileCRD>, Resource<AsymmetricKeysProfileCRD>> getAsymmetricKeysProfileClient(){
        return client.resources(AsymmetricKeysProfileCRD.class);
    }

}