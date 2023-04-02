package br.inatel.thisismeapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("This is Me API")
                        .version("0.5")
                        .description("API para o projeto This is Me")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .summary("This is Me API")
                )
                .addTagsItem(new Tag().name("character-controller").description("Endpoints para o personagem"))
                .addTagsItem(new Tag().name("user-controller").description("Endpoints para o usuário"))
                .addTagsItem(new Tag().name("quest-controller").description("Endpoints para a quest"))
                .addTagsItem(new Tag().name("sub-quest-controller").description("Endpoints para a sub-quest"))
                .addTagsItem(new Tag().name("admin-controller").description("Endpoints para o admin"))
                .addTagsItem(new Tag().name("skill-controller").description("Endpoints para a skill"));
    }
}
