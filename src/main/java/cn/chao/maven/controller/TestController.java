package cn.chao.maven.controller;

import cn.chao.maven.customMapper.UserMapperCustom;
import cn.chao.maven.jpaentity.UsersRepository;
import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.jpaentity.JPATbUsersEntity;
import cn.chao.maven.mapper.UsersEntityMapper;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Log
public class TestController {

    @Autowired
    private UsersEntityMapper usersEntityMapper;
    @Autowired
    private UserMapperCustom userMapperCustom;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("hello")
    public String getHello(){
        return "hello , Spring Boot !!! I think you are a good start .";
    }

    @GetMapping("git")
    public String git(){
        return "hello ,finish Git update. 使用需要的 Git 。并且需要 GitHub 账户进行登录。";
    }

    @GetMapping("mybatis")
    public List<UsersEntity> getUsersEntity(){
        log.fine("执行 MyBatis 的方法。");
        List<UsersEntity> usersEntities = usersEntityMapper.selectByExample(null);
        System.out.println(usersEntities.get(0).getAddress());
        return usersEntities;
    }

    @GetMapping("mybatisAnnoation")
    public UsersEntity getUsersById(int id){
        log.fine("执行 mybatisAnnoation 映射的方法，结果为：" + id);
        UsersEntity usersEntitiesByUserId = userMapperCustom.getUsersEntitiesByUserId(id);
        return usersEntitiesByUserId;
    }

    @GetMapping("mybatisAnnoation2")
    public UsersEntity getUsersByAddress(String address){
        log.fine("执行 mybatisAnnoation2 映射的方法，结果为：" + address);
        UsersEntity usersEntitiesByUserId = userMapperCustom.getUsersEntitiesByAddress(address);
        return usersEntitiesByUserId;
    }

    @GetMapping("jpa")
    public JPATbUsersEntity getJpaResult(long id){
        log.fine("执行 getJpaResult 映射的方法，结果为：" + id);
        Optional<JPATbUsersEntity> usersRepositoryById = usersRepository.findById(id);
        if (usersRepositoryById.isPresent()){   // Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
            JPATbUsersEntity jpaTbUsersEntity = usersRepositoryById.get();
            log.fine("jpaTbUsersEntity 的值不为空，其中的一个值为："+ jpaTbUsersEntity.getAddress());
        }
        JPATbUsersEntity byUid = usersRepository.findByUid(id);
        log.fine("JPA 自定义查询的结果："+byUid.getAddress());
        return usersRepositoryById.get();
    }

    @GetMapping("jpa2")
    public List<JPATbUsersEntity> getJpaResult2(String address){
        log.fine("执行 getJpaResult2 映射的方法，结果为：" + address);
        List<JPATbUsersEntity> all = usersRepository.findAllByAddressContaining(address);
        System.out.println("查看内容：" + all.size());
        return  all;
    }
}
