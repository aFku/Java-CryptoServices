package org.rcbg.afku.CryptoGenerator.k8sClient;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;

public class K8sCrdClientFactory {

    public static MixedOperation<PasswordProfileCR, KubernetesResourceList<PasswordProfileCR>, Resource<PasswordProfileCR>> getPasswordProfileClient(){
        KubernetesClient client = new KubernetesClientBuilder().build();
        return client.resources(PasswordProfileCR.class);
    }

    public static MixedOperation<AsymmetricKeysProfileCR, KubernetesResourceList<AsymmetricKeysProfileCR>, Resource<AsymmetricKeysProfileCR>> getAsymmetricKeysProfileClient(){
        KubernetesClient client = new KubernetesClientBuilder().build();
        return client.resources(AsymmetricKeysProfileCR.class);
    }

}