package com.clt.ess.dao;


import com.clt.ess.entity.User;

import java.util.List;

public interface IUserDao {

    /**
     * 查询用户List
     * @param user
     * @return
     */
    List<User> findUserList(User user);

    /**
     * 查询用户
     * @param userId
     * @return
     */
    User findUserById(String userId);

    List<User> findLoginUserByPersonId(User u);
}
