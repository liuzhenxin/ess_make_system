package com.clt.ess.service;


import com.clt.ess.entity.Person;
import com.clt.ess.entity.User;

import java.util.List;

public interface IUserService {
    /**
     * 查询用户
     * @param user
     * @return
     */
    User findUser(User user);

    /**
     * 查询用户List
     * @param user
     * @return
     */
    List<User> findUserList(User user);

    List<User> findLoginUserByPersonId(User u);

    /**
     * 根据关键字查找人员列表
     * @param keyword 关键词
     */
    List<Person> findPersonListByKeyword(String keyword);
}
