package org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1")
@Group("org.rcbg")
@Kind("AsymmetricKeysProfile")
public class AsymmetricKeysProfileCRD extends CustomResource<AsymmetricKeysProfileSpec, Void> implements Namespaced { }
