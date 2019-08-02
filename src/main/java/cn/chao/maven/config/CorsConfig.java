package cn.chao.maven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 实现基本的跨域请求
 * @author RebortChao
 *
 */
@Configuration
public class CorsConfig {

    /**
     *  '*' 代表全部，'**' 代表适配所有接口
     *  @return
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // ①、setAllowedOrigins(List allowedOrigins);  可以通过这个方法设置多个访问源地址。
        // ②、addAllowedOrigin(String origin) 方法是追加访问源地址。如果不使用”*”（即允许全部访问源），则可以配置多条访问源来做控制。
        //  corsConfiguration.addAllowedOrigin("http://www.systemA.com/");
        //  corsConfiguration.addAllowedOrigin("http://test.systemA.com/");
        //  注意：根据源码可以得知，setAllowedOrigins 会覆盖 this.allowedOrigins。所以在配置访问源地址时，
        //  addAllowedOrigin 方法要写在 setAllowedOrigins 后面
        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用。1、设置访问源 地址
        corsConfiguration.addAllowedHeader("*"); // 允许任何头        2、设置访问源 请求头
        corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）   3、设置访问源 请求方法
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置    4、 对接口设置跨域请求
        return new CorsFilter(source);
    }

    //  注解方式实现跨域请求方法    jdk1.8+     Spring4.2+
    // 可以对 @CrossOrigin 设置特定的访问源，而不是使用默认配置。
    // 可以直接在 @RequestMapping 接口上使用 @CrossOrigin 实现跨域。@CrossOrigin 默认允许所有访问源和访问方法。
    //  @CrossOrigin(origins = "http://domain2.com", maxAge = 3600)
    //  origin="*" 代表所有域名都可访问
    //  maxAge飞行前响应的缓存持续时间的最大年龄，简单来说就是Cookie的有效期 单位为秒。若maxAge是负数,则代表为临时Cookie,不会被持久化,Cookie信息保存在浏览器内存中,浏览器关闭Cookie就消失
}