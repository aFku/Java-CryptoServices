package org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1")
@Group("org.rcbg")
@Kind("PasswordProfile")
public class PasswordProfileCR extends CustomResource<PasswordProfileSpec, Void> implements Namespaced {
    public PasswordProfileCR(){
        super();
    }

    public PasswordProfileCR(String metaName, PasswordProfileSpec spec){
        setMetadata(new ObjectMetaBuilder().withName(metaName).build());
        setSpec(spec);
    }
}
