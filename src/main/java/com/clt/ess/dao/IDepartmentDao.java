package com.clt.ess.dao;

import com.clt.ess.entity.Department;

import java.util.List;

public interface IDepartmentDao {

    List<Department> findDepartmentList(Department dep);
}
