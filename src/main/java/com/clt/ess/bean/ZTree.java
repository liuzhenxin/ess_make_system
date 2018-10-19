package com.clt.ess.bean;

/**
 * 为了与前台所需要的Json数据一致
 * @author pjx
 *
 */
public class ZTree {

	//节点id
	private String id;
	//父id
	private String pId;
	//节点名称
	private String name;
	//节点点击请求
	private String click;
	//是否默认打开节点
	private boolean open;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public String toString() {
		return "ZTree [id=" + id + ", pId=" + pId + ", name=" + name + ", click=" + click + ", open=" + open + "]";
	}
	
}
