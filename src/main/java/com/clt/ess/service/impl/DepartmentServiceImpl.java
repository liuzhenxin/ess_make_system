package com.clt.ess.service.impl;

import com.clt.ess.dao.IDepartmentDao;
import com.clt.ess.entity.Department;
import com.clt.ess.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements IDepartmentService {

    @Autowired
    private IDepartmentDao departmentDao;
    @Override
    public List<Department> findDepartmentList(Department department) {

        return departmentDao.findDepartmentList(department);
    }
}
