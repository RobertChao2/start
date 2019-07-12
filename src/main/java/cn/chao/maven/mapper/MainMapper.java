package cn.chao.maven.mapper;

import java.util.List;

import cn.chao.maven.entity.UsersEntity;
import org.apache.ibatis.annotations.Select;


public interface MainMapper {
	@Select("select * from tb_users where to_days(create_time) = to_days(now());")
	List<UsersEntity> selUsersToday();
	
	@Select("SELECT * FROM tb_users WHERE TO_DAYS( NOW( ) ) - TO_DAYS( create_time)= 1  ")
	List<UsersEntity> selUsersYesterday();
	
	@Select("SELECT * FROM  tb_users WHERE YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now());")
	List<UsersEntity> selUsersYearWeek();
	
	@Select("SELECT * FROM tb_users WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
	List<UsersEntity> selUsersMonth();
}
