package com.tcy.smartai.service.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Configuration
public class SwaggerConfig {
    public static Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
    @Bean
    public Docket docket() {
        logger.info("准备生成接口文档.....");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("古镇监控AI算法接口文档")
                .version("1.0")
                .description("古镇监控AI算法接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("接口")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("package com.tcy.smartai.api"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("开始设置静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
