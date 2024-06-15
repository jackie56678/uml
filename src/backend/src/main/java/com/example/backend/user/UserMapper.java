package com.example.backend.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User findUserByUsername(String username);
    void updateUser(User user);
    @Select("SELECT menu, path FROM role WHERE rid = #{role}")
    List<Map<String,String>>  getMenuList(int role);
}
