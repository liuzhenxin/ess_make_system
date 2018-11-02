package com.clt.ess.dao;


import com.clt.ess.entity.Person;

import java.util.List;

public interface IPersonDao {
    /**
     * 查询个人信息
     * @return
     */
    Person findPerson(Person person);


    Person findPersonById(String personId);
    /**
     * 根据关键字查找人员列表
     * @param keyword 关键词
     */
    List<Person> findPersonListByKeyword(String keyword);
}
