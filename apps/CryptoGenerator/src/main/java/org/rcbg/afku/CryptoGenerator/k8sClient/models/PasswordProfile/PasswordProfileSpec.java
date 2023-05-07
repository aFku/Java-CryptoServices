package org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;

public class PasswordProfileSpec extends PasswordProfileDTO implements KubernetesResource {}