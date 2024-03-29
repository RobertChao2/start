package cn.chao.maven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *  开启 Swagger2-ui 的内容，显示接口信息。
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("cn.chao.maven.controller"))
                .paths(PathSelectors.any()).build();
    }

    /**
     * 构建 API 文档信息
     * @return
     */
    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("文档接口")
                .contact(new Contact("昵称","网址","邮箱"))
                .version("版本")
                .description("描述").build();
    }

}
