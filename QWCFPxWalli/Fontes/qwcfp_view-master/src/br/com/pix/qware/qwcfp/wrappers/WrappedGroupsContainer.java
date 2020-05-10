package br.com.pix.qware.qwcfp.wrappers;

import java.util.List;

public class WrappedGroupsContainer {
	private List<MyGroupsWrapper> list;
	private Long size;
	
	public WrappedGroupsContainer() {
		super();
		//  Auto-generated constructor stub
	}

	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}

	public List<MyGroupsWrapper> getList() {
		return list;
	}

	public void setList(List<MyGroupsWrapper> list) {
		this.list = list;
	}

}
