package cn.chao.maven.customMapper;

import cn.chao.maven.entity.UsersEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapperCustom {

    @Select("select * from tb_users where uid=#{id}")
    public UsersEntity getUsersEntitiesByUserId(@Param("id") int id);

    @Select("select * from tb_users where address LIKE CONCAT('%', #{address}, '%')")
    public UsersEntity getUsersEntitiesByAddress(@Param("address") String address);
}
