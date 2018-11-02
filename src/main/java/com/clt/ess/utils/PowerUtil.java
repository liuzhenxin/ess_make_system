package com.clt.ess.utils;


import com.clt.ess.entity.Unit;
import com.clt.ess.entity.UnitRoleAndPowerRelation;
import com.clt.ess.entity.User;
import com.clt.ess.service.IRoleAndPowerService;
import com.clt.ess.service.IUnitService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用于权限校验
 * 2018.05.23
 * @author pjx
 *
 */
public class PowerUtil {

	/**
	 * 校验当前登录用户是否有操作某个功能的权限
	 * 2018.05.23
	 * @param powerId
	 * @param rAndPowerService
	 * @param unitService
	 * @param topUnitLevel 	配置文件中顶级单位的层级
	 * @param companyLevel 	配置文件中各个公司的层级
	 * @return
	 */
	public static boolean checkUserIsHavaThisPower(HttpSession session, String powerId,
												   IRoleAndPowerService rAndPowerService, IUnitService unitService,
												   Integer topUnitLevel, Integer companyLevel){

		// 获取当前登录对象
		User loginUser = PowerUtil.getLoginUser(session);

		// 首先判断是否为“人员管理功能”,如果是则直接判断当前登录人的等级是否与公司的等级一致,若一致,则有权限
		if("sealTemplatPower_personManager".equals(powerId)||"sealTemplatPower_setup".equals(powerId)
				){

			if(loginUser.getLevel() == companyLevel){

				return true;

			}else{

				return false;

			}

		}


		/*
		 *  1. 如果当前用户所属单位的层级等于配置文件中顶级<单位>的层级,那么直接查询
		 *  (当多个公司合并时，需要为所有公司之上的顶级单位--如诚利通，设置对应的角色以及角色与功能的对应关系)
		 *  2. 如果当前用户所属单位的层级等于配置文件中的顶级<公司>的层级,那么直接查询
		 *  3. 如果当前用户所属单位的层级！不等于配置文件中的顶级<公司>的层级,那么需要先通过方法获取该用户所属的顶级公司的层级
		 */

		// 根据用户所属的单位id查询单位对象
		Unit userUnit = unitService.findUnitById(loginUser.getUnitId());

		if(userUnit == null){
			return false;
		}

		// 判断其所属的单位层级与顶级单位层级的关系、与公司层级的关系
		if(userUnit.getLevel() == topUnitLevel || userUnit.getLevel() == companyLevel){
			// 直接查询单位角色与功能关系表
			// 切割用户信息中的roleId,循环查询角色与功能关系表,有一个有权限则代表有权限
			String roleIds = loginUser.getRoleId();

			String[] roleIdStr = roleIds.split("@");

			if(roleIdStr != null && roleIdStr.length >= 1){

				for (String roleId : roleIdStr) {

					UnitRoleAndPowerRelation unitRoleAndPowerRelation = rAndPowerService.queryByRoleIdAndPowerIdAndTopUnitId(roleId, powerId, userUnit.getUnitId());

					if(unitRoleAndPowerRelation != null){
						// 如果查询到的对象不为空,则说明无权限
						return true;

					}

				}

			}else{

				return false;

			}

			// 如果循环完成之后,也没有进行返回,则说明没有权限
			return false;

		}else{
			// 先获取当前单位所属的公司单位对象
			Unit companyUnit = unitService.queryCompanyUnitByUserParentUnitId(userUnit.getParentUnitId());

			if(companyUnit == null){

				return false;

			}


			// 切割用户信息中的roleId,循环查询角色与功能关系表,有一个有权限则代表有权限
			String roleIds = loginUser.getRoleId();

			String[] roleIdStr = roleIds.split("@");

			if(roleIdStr != null && roleIdStr.length >= 1){

				for (String roleId : roleIdStr) {

					UnitRoleAndPowerRelation unitRoleAndPowerRelation = rAndPowerService.queryByRoleIdAndPowerIdAndTopUnitId(roleId, powerId, companyUnit.getUnitId());

					if(unitRoleAndPowerRelation != null){
						// 如果查询到的对象不为空,则说明无权限
						return true;

					}

				}

			}else{

				return false;

			}
			// 如果循环完成之后,也没有进行返回,则说明没有权限
			return false;

		}

	}


	/**
	 * 判断当前登录的用户是否有点击的当前单位的权限
	 * 2018.05.24
	 * @param
	 * @param currentUnit  当前点击的单位
	 * @param topUnitLevel 配置文件中顶级单位的层级
	 * @return
	 */
	public static boolean checkUserIsHaveThisRangePower(HttpSession session, Unit currentUnit, Integer topUnitLevel){

		// 获取当前登录对象
		User loginUser = PowerUtil.getLoginUser(session);
		// 根据当前登录用户的管理范围判断是否有当前层级的管理权限
		return judgeAuthority(loginUser.getPowerRange(), currentUnit.getLevel(), loginUser.getLevel(), topUnitLevel);
	}

	/**
	 * 根据操作类型，单位等级和管理员等级来判断权限
	 * 2018.05.23
	 * @param powerRanger 管理范围
	 * @param unitLevel 单位层级
	 * @param topUnitLevel 配置文件中顶级单位的层级
	 * @return 结果
	 */
	public static boolean judgeAuthority(Integer powerRanger,Integer unitLevel,Integer userLevel,Integer topUnitLevel){

		Integer val = unitLevel - userLevel;

		if(powerRanger == 1){
			// 第一种情况--本级管理员用户只有本级的权限
			return val == 0;

		}else if(powerRanger == 2){
			// 第二种情况--只能给直属下级做，最高级别自己做
			return (userLevel == 0 && unitLevel == 0) || val == 1;

		}else if(powerRanger == 3){
			// 第三种情况--给自己和直属下级做
			return val == 0 || val == 1;

		}else if(powerRanger == 4){
			// 第四种情况--给自己和所有下级做
			return val >= 0;

		}else if(powerRanger == 5){
			// 第五种情况--只能最高级做
			return userLevel == topUnitLevel;
		}else{
			// 传入其他值，说明不具有该权限
			return false;
		}
	}

	/**
	 * 获取当前登录对象
	 * 2018.05.23
	 * @param session
	 * @return
	 */
	public static User getLoginUser(HttpSession session){
		return (User)session.getAttribute("user");
	}

	/**
	 * 删除session中的登录对象
	 * 2018.06.05
	 * @param session session
	 * @return
	 */
	public static void delLoginUserFromSession(HttpSession session){
		session.removeAttribute("user");
	}

	/**
	 * 将当前登录对象放入session中
	 * 2018.06.05
	 * @param session
	 * @return
	 */
	public static void addLoginUserToSession(HttpSession session, User loginUser){
		session.setAttribute("user", loginUser);
	}

	/**
	 * 将当前登录对象从session中删除,然后添加新的登录对象
	 * 2018.06.05
	 * @param session
	 * @return
	 */
	public static void delOldLoginUserAndAddNewLoginUserAboutSession(HttpSession session, User loginUser){

		delLoginUserFromSession(session);

		addLoginUserToSession(session, loginUser);

	}

	/**
	 * 根据管理范围的值获取管理范围的名称
	 * 2018.05.29
	 * @param powerRange
	 * @return
	 */
	public static String getPowerRangeNameByPowerRange(Integer powerRange){

		if(powerRange == 1){

			return "本级管理员有本级的操作权限";

		}else if(powerRange == 2){

			return "本级管理员只有其直属下级的操作权限（最高层级管理员有其本级的操作权限）";

		}else if(powerRange == 3){

			return "本级管理员有本级和直属下级的操作权限";

		}else if(powerRange == 4){

			return "本级管理员有本级和所有下级的操作权限";

		}else if(powerRange == 5){

			return "只有最高层级的管理员有操作权限";

		}else if(powerRange == 6){

			return "无权限";

		}else{
			return "无权限";

		}
	}

}
