package com.sky.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.result.Result;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time," +
            " create_user, update_user) VALUES (#{name},#{username},#{password},#{phone},#{sex},#{idNumber}," +
            "#{status}," +
            "#{createTime},#{updateTime},#{createUser},#{updateUser})")
    boolean save(Employee employee);

    Page<Employee> page(String name);

    @Update("update employee set status=#{status} where id=#{id}")
    void state(Integer status, Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    Employee getById(Long id);
}
