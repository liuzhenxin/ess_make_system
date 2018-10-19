package com.clt.ess.service;

import com.clt.ess.entity.Department;

import java.util.List;

public interface IDepartmentService {

    List<Department> findDepartmentList(Department department);
}
