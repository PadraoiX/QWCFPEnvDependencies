package br.com.pix.qware.qwcfp.util;

import br.com.qwcss.ws.dto.GroupDTO;

public class GroupTreeNode implements ITreeNode{
	
	private String hashId;
	private GroupDTO group;

	/**
	 * @param hashId
	 */
	public GroupTreeNode(GroupDTO group) {
		super();
		hashId = GroupDTO.class.toString() + group.getGroupId();
	}

	@Override
	public String getHashId() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public Object getContent() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public Class<GroupDTO> getClassType() {
		return  GroupDTO.class;
	}
	
	

}
