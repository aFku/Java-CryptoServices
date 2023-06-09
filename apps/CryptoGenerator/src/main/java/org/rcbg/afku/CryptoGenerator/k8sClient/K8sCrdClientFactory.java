package org.rcbg.afku.CryptoGenerator.k8sClient;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.services.ProfilesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class K8sCrdClientFactory {

    private KubernetesClient client;

    private final static Logger logger = LoggerFactory.getLogger(ProfilesManager.class);

    public K8sCrdClientFactory(){
        this.client = new KubernetesClientBuilder().build();
    }

    public K8sCrdClientFactory(Config config){
        this.client = new KubernetesClientBuilder().withConfig(config).build();
    }

    public MixedOperation<PasswordProfileCR, KubernetesResourceList<PasswordProfileCR>, Resource<PasswordProfileCR>> passwordsClient(){
        return this.client.resources(PasswordProfileCR.class);
    }

    public MixedOperation<AsymmetricKeysProfileCR, KubernetesResourceList<AsymmetricKeysProfileCR>, Resource<AsymmetricKeysProfileCR>> asymmetricKeysClient(){
        return this.client.resources(AsymmetricKeysProfileCR.class);
    }

}