package com.tcy.smartai.service.dao.impl;

import com.tcy.smartai.service.dao.UserDao;
import com.tcy.smartai.service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addUser(User user) {
        mongoTemplate.insert(user);

    }

    @Override
    public User updateUser(User user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update().set("name", user.getName());
        mongoTemplate.updateFirst(query, update, User.class);
        return user;
    }

    @Override
    public void removeUser(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, User.class);
    }

    @Override
    public User findOne(User user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public List<User> findMore(User user) {
        Query query = new Query(Criteria.where("username").is(user.getName()));
        return mongoTemplate.find(query, User.class);
    }
}
