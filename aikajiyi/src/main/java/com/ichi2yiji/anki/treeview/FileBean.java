package com.ichi2yiji.anki.treeview;


import java.io.Serializable;

public class FileBean implements Serializable
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String desc;

	@TreeNodeDepth
	private String depth;
	@TreeNodeDid
	private String did;
	@TreeNodeLrnCount
	private String lrnCount;
	@TreeNodeNewCount
	private String newCount;
	@TreeNodeRevCount
	private String revCount;


	public FileBean(int _id, int parentId, String name)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
	}

	public FileBean(int _id, int parentId, String name, String depth, String did, String lrnCount, String newCount, String revCount)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		this.depth = depth;
		this.did = did;
		this.lrnCount = lrnCount;
		this.newCount = newCount;
		this.revCount = revCount;
	}

}
