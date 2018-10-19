package com.clt.ess.service.impl;



import com.clt.ess.dao.IPersonDao;
import com.clt.ess.dao.IUserDao;
import com.clt.ess.entity.Person;
import com.clt.ess.entity.User;
import com.clt.ess.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    IUserDao userDao = null;
    @Autowired
    IPersonDao personDao = null;
    @Override
    public User findUser(User user) {
        return userDao.findUserById(user.getUserId());
    }

    @Override
    public List<User> findUserList(User user) {
        return userDao.findUserList(user);
    }

    @Override
    public List<User> findLoginUserByPersonId(User u) {
        return userDao.findLoginUserByPersonId(u);
    }

    @Override
    public List<Person> findPersonListByKeyword(String keyword) {
        return personDao.findPersonListByKeyword(keyword);
    }
}
