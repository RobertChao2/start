package cn.chao.maven.jpaentity;

import cn.chao.maven.entity.UsersEntity;
import cn.chao.maven.jpaentity.JPATbUsersEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * 声明一个接口继承于CrudRepository 或者 PagingAndSortingRepository，JpaRepository,Repository
 */
public interface UsersRepository extends CrudRepository<JPATbUsersEntity, Long> {

    public @Select({"SELECT *",
            "FROM users_entity",
            "WHERE uid = #{uid}"})
    JPATbUsersEntity findByUid(Long uid);

    public @Select({"SELECT *",
            "FROM j_p_a_tb_users_entity",
            "WHERE address LIKE CONCAT('%', #{addressPattern}, '%')"})
    List<JPATbUsersEntity> findAllByAddressContaining(@Param("addressPattern") String addressPattern);
}
