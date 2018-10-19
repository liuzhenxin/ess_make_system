package com.clt.ess.service.impl;


import com.clt.ess.base.Constant;
import com.clt.ess.bean.ZTree;
import com.clt.ess.dao.IIssuerUnitDao;
import com.clt.ess.dao.IUnitDao;
import com.clt.ess.entity.IssuerUnit;
import com.clt.ess.entity.Unit;
import com.clt.ess.service.IUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UnitServiceImpl implements IUnitService {

    @Autowired
    private IUnitDao unitDao ;

    private List<Unit> menuUnit = new ArrayList<>();
    @Autowired
    private IIssuerUnitDao issuerUnitDao ;


    private Unit topUnit = null;
    @Override
    public List<ZTree> queryUnitMenu(String unitId) {

        menuUnit.clear();
        //根据unit获取子单位
        Unit rootUnit = unitDao.findUnitByUnitId(unitId);
        menuUnit.add(rootUnit);
        List<Unit> UnitMenus = unitDao.findUnitByParentUnitId(unitId);
        //递归排列顺序
        getMenuList(UnitMenus);

        //将菜单的内容整合到一个与前台zTree对应的类的集合中,为了与ZTree的json格式对应
        List<ZTree> treeList = new ArrayList<ZTree>();
        for (Unit unitMenu : menuUnit) {
            ZTree ztree = new ZTree();
            ztree.setId(unitMenu.getUnitId());
            ztree.setName(unitMenu.getUnitName());
            ztree.setpId(unitMenu.getParentUnitId());
            ztree.setClick("javaScript:onclickNode('"+unitMenu.getUnitId()+"');");
            if(unitMenu.getLevel() == 0){
                ztree.setOpen(true);
            }else{
                ztree.setOpen(false);
            }
            treeList.add(ztree);
        }
        return treeList;
    }

    @Override
    public Unit findUnitById(String unitId) {

        return unitDao.findUnitByUnitId(unitId);
    }


    @Override
    public Unit findTopUnitByChildUnitId(String unitId) {

        Unit childUnit = unitDao.findUnitByUnitId(unitId);
        return childUnit;
    }

    /**
     * 递归函数
     * @param unitMenus
     */
    private void getMenuList(List<Unit> unitMenus) {

        for (Unit nav : unitMenus) {
            menuUnit.add(nav);
            if(nav.getMenus()!=null){
                getMenuList(nav.getMenus());
            }
        }
    }


    /**
     * 获取当前单位的一级单位
     * @param unitId
     * @return
     */

    public Unit findTopUnit(String unitId ){
        //获取一级单位的印章类型
        //获取当前单位的一级单位
        Unit TopUnit = unitDao.findUnitByUnitId(unitId);
        for(int i = 0; i<10000;i++){
            if("0".equals(TopUnit.getParentUnitId())){
                //退出循环
                break;
            }else{
                //更新临时对象
                TopUnit = unitDao.findUnitByUnitId(TopUnit.getParentUnitId());
            }
        }
        return TopUnit;
    }

    /**
     * 根据一级单位获取可以使用的证书授权单位
     * @param unitId
     * @return
     */
    @Override
    public List<IssuerUnit> findIssuerUnitByUnitId(String unitId) {
        List<IssuerUnit> issuerUnitList = new ArrayList<>();
        String value = issuerUnitDao.findIssuerUnitValueByUnitId(unitId);
        String [] valueList = value.split("@");
        // 增强for形式  s遍历所有数组
        for(String s:valueList){
            IssuerUnit issuerUnit = issuerUnitDao.findIssuerUnitById(s);
            if(issuerUnit!=null){
                issuerUnitList.add(issuerUnit);
            }
        }
        return issuerUnitList;
    }

    public Unit queryCompanyUnitByUserParentUnitId(String parentUnitId) {

        /*
         * 将传入的父id作为单位id查询单位对象,判断其单位对象的层级是否为配置文件中
         * 顶级单位(公司)的层级,若是则返回单位对象,若不是,则递归查询
         */
        Unit unit = unitDao.findUnitByUnitId(parentUnitId);

        if(unit != null && unit.getLevel() != Constant.companyLevel){

            queryCompanyUnitByUserParentUnitId(unit.getParentUnitId());

        }else if(unit != null && unit.getLevel() == Constant.companyLevel){

            topUnit = unit;

            return topUnit;

        }
        return topUnit;

    }


    /**
     * 查找当前单位直到顶层单位的名称集合链
     */
    public String getUnitNameChain(String unitId){
        String unitNameChain = "";
        Unit TopUnit = unitDao.findUnitByUnitId(unitId);
        unitNameChain = TopUnit.getUnitName()+"-"+unitNameChain;
        for(int i = 0; i<10000;i++){
            if("0".equals(TopUnit.getParentUnitId())){
                //退出循环
                break;
            }else{
                //更新临时对象
                TopUnit = unitDao.findUnitByUnitId(TopUnit.getParentUnitId());
                unitNameChain = TopUnit.getUnitName()+"-"+unitNameChain;
            }
        }
        return unitNameChain;

    }

}
