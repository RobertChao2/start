package cn.chao.maven.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 登录界面的验证码配置信息
 *   这里是 Java 配置方式，XML 配置方式可以将配置信息配置在 WEB,XML 中。
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer KaptchaProducer() {
        Properties kaptchaProperties = new Properties();
        kaptchaProperties.put("kaptcha.border", "no");
        kaptchaProperties.put("kaptcha.textproducer.char.length","4");
        kaptchaProperties.put("kaptcha.image.height","50");
        kaptchaProperties.put("kaptcha.image.width","150");
        kaptchaProperties.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        kaptchaProperties.put("kaptcha.textproducer.font.color","black");
        kaptchaProperties.put("kaptcha.textproducer.font.size","40");
        kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        //kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.DefaultNoise");
        kaptchaProperties.put("kaptcha.textproducer.char.string","acdefhkmnprtwxy2345678");
        Config config = new Config(kaptchaProperties);
        return config.getProducerImpl();
    }
}