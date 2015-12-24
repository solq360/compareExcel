package org.solq.excel.compare;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CompareBlockProcessor implements IProcessor {

	public boolean compare(String newFile, String oldFile,
			Map<String, Map<Integer, List<String>>> newbody,
			Map<String, Map<Integer, List<String>>> oldbody) {
		// 新增表
		// 删除表
		compareTable(newFile, oldFile, newbody, oldbody);
		// 新增行
		// 删除行
		compareRow(newFile, oldFile, newbody, oldbody);
		return true;
	}

	void compareTable(String newFile, String oldFile,
			Map<String, Map<Integer, List<String>>> newbody,
			Map<String, Map<Integer, List<String>>> oldbody) {

		Set<String> newTable = new HashSet<String>(newbody.keySet());
		Set<String> newTable2 = new HashSet<String>(newbody.keySet());
		Set<String> oldTable = new HashSet<String>(oldbody.keySet());

		newTable.removeAll(oldTable);
		oldTable.removeAll(newTable2);

		// 新增表
		if (!newTable.isEmpty()) {
			for (String table : newTable) {
				newbody.remove(table);
				System.out.println( newFile + " 新增表 : " + " : " + table);
			}
		}

		// 删除表
		if (!oldTable.isEmpty()) {
			for (String table : oldTable) {
				oldbody.remove(table);
				System.out.println(oldFile + " 删除表  : " + " : " + table);
			}
		}
	}

	void compareRow(String newFile, String oldFile,
			Map<String, Map<Integer, List<String>>> newbody,
			Map<String, Map<Integer, List<String>>> oldbody) {

		// 新增行
		// 删除行
		//TODO 未完成 暂时未知算法
		for(Entry<String, Map<Integer, List<String>>> entry : newbody.entrySet()){
			final String key = entry.getKey();
			 Map<Integer, List<String>> newTable = newbody.get(key);
			 Map<Integer, List<String>> oldTable = oldbody.get(key);
			 
			 int newIndex =0;
			 int oldIndex = 0;
			 
			 int mazSize = Math.max(newTable.size(), oldTable.size());
//			 for(int i =0;i<mazSize;i++){
//				 List<String> newData = newTable.get(newIndex);
//				 int tIndex= oldIndex;
//				 
//				 while(tIndex++ < mazSize){
//					 List<String> oldData = oldTable.get(tIndex);
//					 if(checkData(newData,oldData)){
//						 
//					 }
//				 }
//			 }
		}
	}

	private boolean checkData(List<String> newData, List<String> oldData) {
		if(newData==null || oldData==null){
			return false;
		}
		
 		return false;
	}

}
