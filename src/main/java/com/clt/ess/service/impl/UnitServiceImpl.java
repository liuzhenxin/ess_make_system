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
    private IUnitDao unitDao;
    @Autowired
    private IIssuerUnitDao issuerUnitDao;


    //菜单list
    private List<Unit> menu = new ArrayList<>();
    //顶级单位
    private Unit topUnit = null;
    /**
     * 根据单位ID查询单位列表
     * @param unitId 单位Id
     * @return ZTree 树
     */
    @Override
    public List<ZTree> queryUnitMenu(String unitId) {
        //清空
        menu.clear();
        //根据unit获取根单位
        Unit rootUnit = unitDao.findUnitByUnitId(unitId);
        //添加根单位到单位列表
        menu.add(rootUnit);
        //根单位的所有子单位
        List<Unit> UnitMenus = unitDao.findUnitByParentUnitId(unitId);

        //递归组织成菜单list
        getMenuList(UnitMenus);

        //将菜单的内容整合到一个与前台zTree对应的类的集合中,为了与ZTree的json格式对应
        List<ZTree> treeList = new ArrayList<ZTree>();
        for (Unit unitMenu : menu) {
            ZTree ztree = new ZTree();
            ztree.setId(unitMenu.getUnitId());
            ztree.setName(unitMenu.getUnitName());
            ztree.setpId(unitMenu.getParentUnitId());
            //点击节点执行的语句
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

    /**
     * 根据单位ID查找unit对象
     * @param unitId 单位Id
     * @return 单位对象
     */
    @Override
    public Unit findUnitById(String unitId) {
        return unitDao.findUnitByUnitId(unitId);
    }

    /**
     *将查询到单位对象，递归组织成菜单list
     * @param unitMenus 单位对象
     */
    private void getMenuList(List<Unit> unitMenus) {
        for (Unit nav : unitMenus) {
            menu.add(nav);
            //如果此单位拥有子单位
            if(nav.getMenus()!=null){
                getMenuList(nav.getMenus());
            }
        }
    }
    /**
     * 获取当前单位的一级单位
     * @param unitId 单位ID
     * @return 一级单位对象
     */
    public Unit findTopUnit(String unitId){
        //临时一级单位对象
        Unit TopUnit = unitDao.findUnitByUnitId(unitId);
        for(int i = 0; i<10000;i++){
            //判断层级level,如果为1 则此单位为一级单位
            if(Constant.companyLevel==TopUnit.getLevel()){
                //退出循环
                break;
            }else{
                //如果不是一级单位，根据其父ID查找上级单位作为新的临时一级单位独享。
                TopUnit = unitDao.findUnitByUnitId(TopUnit.getParentUnitId());
            }
        }
        return TopUnit;
    }

    /**
     * 根据一级单位获取可以使用的证书授权单位
     * @param unitId 单位ID
     * @return
     */
    @Override
    public List<IssuerUnit> findIssuerUnitByUnitId(String unitId) {
        //可用的证书授权单位
        List<IssuerUnit> issuerUnitList = new ArrayList<>();
        //根据单位ID 在独立单位配置参数表查询可用的证书授权单位值
        String value = issuerUnitDao.findIssuerUnitValueByUnitId(unitId);
        //value 示例001@002@003  分割出证书授权单位的ID
        String [] valueList = value.split("@");
        //根据授权单位ID集合遍历查找出授权单位对象
        for(String issuerUnitId:valueList){
            //根据授权单位Id查询对象
            IssuerUnit issuerUnit = issuerUnitDao.findIssuerUnitById(issuerUnitId);
            //如果查询到的对象不为空，添加到可用授权单位列表
            if(issuerUnit!=null){
                issuerUnitList.add(issuerUnit);
            }
        }
        return issuerUnitList;
    }

    /**
     * 获取当前单位的一级单位
     * @param parentUnitId 父ID
     * @return 一级单位对象
     */
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
