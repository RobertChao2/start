package cn.chao.maven.mapper;

import cn.chao.maven.entity.LogEntity;
import cn.chao.maven.entity.LogEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LogEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    long countByExample(LogEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int deleteByExample(LogEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int insert(LogEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int insertSelective(LogEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    List<LogEntity> selectByExample(LogEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    LogEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LogEntity record, @Param("example") LogEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LogEntity record, @Param("example") LogEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LogEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LogEntity record);
}