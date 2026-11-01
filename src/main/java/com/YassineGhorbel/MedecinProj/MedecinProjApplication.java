package com.YassineGhorbel.MedecinProj;


import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
public class MedecinProjApplication implements RepositoryRestConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(MedecinProjApplication.class, args);
	}



	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		config.exposeIdsFor(Medecin.class, Specialite.class);
		cors.addMapping("/**")
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}
}
