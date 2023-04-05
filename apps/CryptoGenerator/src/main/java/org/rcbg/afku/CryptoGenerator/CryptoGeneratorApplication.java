package org.rcbg.afku.CryptoGenerator;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import org.rcbg.afku.CryptoGenerator.k8sClient.K8sCrdClientFactory;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCRD;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class CryptoGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoGeneratorApplication.class, args);
	}

}
