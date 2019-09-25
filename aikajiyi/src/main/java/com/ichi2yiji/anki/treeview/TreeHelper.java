package com.ichi2yiji.anki.treeview;



import com.chaojiyiji.yiji.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367
 * @author zhy
 *
 */
public class TreeHelper {
	/**
	 * 传入我们的普通bean，转化为我们排序后的Node
	 *
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> List<Node> getSortedNodes(List<T> datas,
												int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException

	{
		List<Node> result = new ArrayList<Node>();
		// 将用户数据转化为List<Node>
		List<Node> nodes = convetData2Node(datas);
		// 拿到根节点
		List<Node> rootNodes = getRootNodes(nodes);
		// 排序以及设置Node间关系
		for (Node node : rootNodes) {
			addNode(result, node, defaultExpandLevel, 1);
		}
		return result;
	}

	/**
	 * 过滤出所有可见的Node
	 *
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNode(List<Node> nodes) {
		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes) {
			// 如果为跟节点，或者上层目录为展开状态
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 将我们的数据转化为树的节点
	 *
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static <T> List<Node> convetData2Node(List<T> datas)
			throws IllegalArgumentException, IllegalAccessException

	{
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;

		for (T t : datas) {
			int id = -1;
			int pId = -1;
			String label = null;
			String depth = null;
			String did = null;
			String lrnCount = null;
			String newCount = null;
			String revCount = null;
			Class<? extends Object> clazz = t.getClass();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field f : declaredFields) {
				if (f.getAnnotation(TreeNodeId.class) != null) {
					f.setAccessible(true);
					id = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodePid.class) != null) {
					f.setAccessible(true);
					pId = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodeLabel.class) != null) {
					f.setAccessible(true);
					label = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeDepth.class) != null) {
					f.setAccessible(true);
					depth = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeDid.class) != null) {
					f.setAccessible(true);
					did = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeLrnCount.class) != null) {
					f.setAccessible(true);
					lrnCount = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeNewCount.class) != null) {
					f.setAccessible(true);
					newCount = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeRevCount.class) != null) {
					f.setAccessible(true);
					revCount = (String) f.get(t);
				}
				if (id != -1 && pId != -1 && label != null && depth != null && did != null && lrnCount != null && newCount != null && revCount != null) {
					break;
				}
			}
			node = new Node(id, pId, label, depth, did, lrnCount, newCount, revCount, null);
			nodes.add(node);
		}

		/**
		 * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
		 */
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				Node m = nodes.get(j);
				if (m.getpId() == n.getId()) {
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getpId()) {
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}

		// 设置图片
		for (Node n : nodes) {
			setNodeIcon(n);
		}
		return nodes;
	}

	private static List<Node> getRootNodes(List<Node> nodes) {
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes) {
			if (node.isRoot())
				root.add(node);
		}
		return root;
	}

	/**
	 * 把一个节点上的所有的内容都挂上去
	 */
	private static void addNode(List<Node> nodes, Node node,
								int defaultExpandLeval, int currentLevel) {

		nodes.add(node);
		if (defaultExpandLeval >= currentLevel) {
			node.setExpand(true);
		}

		if (node.isLeaf())
			return;
		for (int i = 0; i < node.getChildren().size(); i++) {
			addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
					currentLevel + 1);
		}
	}

	/**
	 * 设置节点的图标
	 *
	 * @param node
	 */
	private static void setNodeIcon(Node node) {
		if (node.getChildren().size() > 0 && node.isExpand()) {
//			node.setIcon(R.drawable.tree_ex);
			node.setIcon(R.drawable.minus_new);
		} else if (node.getChildren().size() > 0 && !node.isExpand()) {
//			node.setIcon(R.drawable.tree_ec);
			node.setIcon(R.drawable.plus_new);
		} else
			node.setIcon(-1);

	}

	///////////////////////dx  add
	public static boolean isSetNodeBlanck(Node node) {
		/*if (node.getChildren().size() > 0) {
			//Node节点有子节点
			if (!node.isExpand()) {
				//Node节点未展开
				return true;
			} else {
				return false;
			}
		} else {
			//Node没有子节点
			if(node.getParent()!=null&&node==node.getParent().getChildren().get(0)){
				//Node有父节点
				return false;
			}else{
				return true;
			}
		}*/
		if (node.getChildren().size() > 0||!node.isLeaf()) {
				return false;
		} else {
			//Node没有子节点
			if(node.getParent()!=null&&node==node.getParent().getChildren().get(0)){
				//Node有父节点
				return false;
			}else{
				return true;
			}
		}
	}

	/**
	 * 判断节点是根节点、子节点、叶节点及其展开状态
	 * @param node
	 * @return
     */
	public static int nodeIsRootOrChildOrLeaf(Node node){
		if (node.isRoot() && !node.isExpand()){
			return 0;//根节点且未展开
		}else if(node.isRoot() && node.isExpand()){
			return 1;//根节点且展开
		}else if(node.getChildren().size() > 0 && !node.isExpand() && (node.getParent().getChildren().get(node.getParent().getChildren().size()-1)== node)){
			return 3;//子节点非叶节点,未展开,且是最后一个子节点
		}else if(node.getChildren().size() > 0 && !node.isExpand() && (node.getParent().getChildren().get(node.getParent().getChildren().size()-1)!= node)){
			return 2;//子节点非叶节点,未展开,且不是最后一个子节点
		}else if(node.getChildren().size() > 0 && node.isExpand()){
			return 2;//子节点非叶节点且展开
		}else if(node.isLeaf() && ((findRootNode(node).getChildren().size() -1) == node.getId())){
			return 3;//叶节点且是最后的一个叶节点
		}else if(node.isLeaf() && (node.getParent().getChildren().size()-1) != node.getId()){
			return 2;//叶节点但不是最后的一个叶节点
		}
		else{
			//叶节点
			return 3;
		}
	}

	private static Node findRootNode(Node node){
		Node n ;
		if(node.isRoot()){
			n = node;
		}else {
			n = findRootNode(node.getParent());
		}
		return n;
	}

//	private static int childrenNumber(Node node){
//		int num = 0;
////		if(node.isLeaf()){
////			num++;
////		}else{
////			num = node.getChildren().size();
////		}
//		if(node.getChildren().size() > 0){
//			num = node.getChildren().size();
//		}else{
//			num = num + 1;
//		}
//		return num;
//	}
}

