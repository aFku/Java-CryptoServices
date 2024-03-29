package org.rcbg.afku.CryptoPass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CryptoPassApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoPassApplication.class, args);
	}

}
