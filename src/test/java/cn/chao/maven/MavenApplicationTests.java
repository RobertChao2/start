package cn.chao.maven;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MavenApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testSet() {
        this.redisTemplate.opsForValue().set("study", "redis");
        System.out.println(this.redisTemplate.opsForValue().get("study"));
    }
}
