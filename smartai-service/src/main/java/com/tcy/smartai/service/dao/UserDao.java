package com.tcy.smartai.service.dao;

import com.tcy.smartai.service.entity.User;

import java.util.List;

public interface UserDao {

    void addUser(User user);

    User updateUser(User user);

    void removeUser(String id);

    User  findOne(User user);

    List<User> findMore(User user);


}
