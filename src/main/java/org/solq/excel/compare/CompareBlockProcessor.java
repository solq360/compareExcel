package org.solq.excel.compare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.solq.excel.FilterInfo;

public class CompareBlockProcessor implements IProcessor {

    public boolean compare(String newFile, String oldFile, Map<String, Map<Integer, List<String>>> newbody, Map<String, Map<Integer, List<String>>> oldbody) {
	// 新增表
	// 删除表
	compareTable(newFile, oldFile, newbody, oldbody);
	// 新增行
	// 删除行
	compareRow(newFile, oldFile, newbody, oldbody);
	return true;
    }

    void compareTable(String newFile, String oldFile, Map<String, Map<Integer, List<String>>> newbody, Map<String, Map<Integer, List<String>>> oldbody) {

	Set<String> newTable = new HashSet<String>(newbody.keySet());
	Set<String> newTable2 = new HashSet<String>(newbody.keySet());
	Set<String> oldTable = new HashSet<String>(oldbody.keySet());

	newTable.removeAll(oldTable);
	oldTable.removeAll(newTable2);

	// 新增表
	if (!newTable.isEmpty()) {
	    for (String table : newTable) {
		newbody.remove(table);
		System.out.println("A 新增表 : " + " : " + table);
	    }
	}

	// 删除表
	if (!oldTable.isEmpty()) {
	    for (String table : oldTable) {
		oldbody.remove(table);
		System.out.println("B 删除表  : " + " : " + table);
	    }
	}
    }

    void compareRow(String newFile, String oldFile, Map<String, Map<Integer, List<String>>> newbody, Map<String, Map<Integer, List<String>>> oldbody) {

	String t = "\t";
	// 过滤相同行数据
	// 取主建
	// 取行数
	for (Entry<String, Map<Integer, List<String>>> entry : newbody.entrySet()) {
	    List<FilterInfo> filterInfos = new LinkedList<FilterInfo>();

	    final String key = entry.getKey();
	    Map<Integer, List<String>> newTable = newbody.get(key);
	    Map<Integer, List<String>> oldTable = oldbody.get(key);
	    final int mazSize = Math.max(newTable.size(), oldTable.size());

	    filterEqual(newTable, oldTable, mazSize);
	    filterEqual(oldTable, newTable, mazSize);

	    List<FilterInfo> infos = filterKey(newTable, oldTable, mazSize);
	    filterInfos.addAll(infos);

	    infos = filterRow(newTable, oldTable, mazSize);
	    filterInfos.addAll(infos);
	    if (filterInfos.isEmpty()) {
		continue;
	    }
	    System.out.println(key);
	    for (FilterInfo info : filterInfos) {
		System.out.println("--------------------------------");
		System.out.println(t + "A" + " : 行数 " + info.getNewIndex() + info.toNewData());
		System.out.println(t + "B" + " : 行数 " + info.getOldIndex() + info.toOldData());
	    }
	}

    }

    /***
     * 取行数
     */
    private List<FilterInfo> filterRow(Map<Integer, List<String>> newTable, Map<Integer, List<String>> oldTable, int maxSize) {

	List<FilterInfo> filterInfos = new ArrayList<>(maxSize);
	for (int i = 0; i < maxSize; i++) {
	    List<String> newData = newTable.get(i);
	    List<String> oldData = oldTable.get(i);
	    if (newData == null && oldData == null) {
		continue;
	    }
	    filterInfos.add(FilterInfo.of(i, newData, i, oldData));
	}
	return filterInfos;
    }

    /***
     * 过滤相同行数据
     */
    private List<FilterInfo> filterKey(Map<Integer, List<String>> newTable, Map<Integer, List<String>> oldTable, int maxSize) {
	int newIndex = 0;
	int oldIndex = 0;
	List<FilterInfo> filterInfos = new ArrayList<>(maxSize);
	for (int i = 0; i < maxSize; i++) {
	    List<String> newData = newTable.get(newIndex);
	    if (newData == null || newData.size() < 2) {
		newIndex++;
		continue;
	    }
	    int tIndex = oldIndex - 1;
	    while (tIndex++ < maxSize) {
		List<String> oldData = oldTable.get(tIndex);
		if (oldData == null || oldData.size() < 2) {
		    continue;
		}
		int checkCelIndex = 1;
		if (!checkCell(newData, oldData, checkCelIndex)) {
		    continue;
		}
		oldTable.remove(tIndex);
		newTable.remove(newIndex);

		filterInfos.add(FilterInfo.of(newIndex, newData, tIndex, oldData));
		oldIndex++;
		break;
	    }
	    newIndex++;
	}
	return filterInfos;
    }

    private boolean checkCell(List<String> newData, List<String> oldData, int index) {
	String n = newData.get(index);
	String o = oldData.get(index);
	if (n == null) {
	    n = "";
	}
	if (o == null) {
	    o = "";
	}
	return n.trim().equals(o.trim());
    }

    /***
     * 过滤相同行数据
     */
    private void filterEqual(Map<Integer, List<String>> newTable, Map<Integer, List<String>> oldTable, int maxSize) {
	int newIndex = 0;
	int oldIndex = 0;

	for (int i = 0; i < maxSize; i++) {
	    List<String> newData = newTable.get(newIndex);
	    if (newData == null) {
		newIndex++;
		continue;
	    }
	    int tIndex = oldIndex - 1;
	    while (tIndex++ < maxSize) {
		List<String> oldData = oldTable.get(tIndex);
		if (!checkData(newData, oldData)) {
		    continue;
		}
		oldTable.remove(tIndex);
		newTable.remove(newIndex);
		oldIndex++;
		break;
	    }
	    newIndex++;
	}
    }

    private boolean checkData(List<String> newData, List<String> oldData) {
	if (newData == null || oldData == null) {
	    return false;
	}

	if (newData.size() == oldData.size()) {
	    for (int i = 0; i < newData.size(); i++) {
		if (!checkCell(newData, oldData, i)) {
		    return false;
		}
	    }
	}
	int newLastEmptyIndex = findLastEmptyIndex(newData);
	int oldLastEmptyIndex = findLastEmptyIndex(oldData);
	if (newLastEmptyIndex != oldLastEmptyIndex) {
	    return false;
	}
	// 二次检查
	for (int i = 0; i < newLastEmptyIndex; i++) {
	    if (!checkCell(newData, oldData, i)) {
		return false;
	    }
	}
	return true;
    }

    private int findLastEmptyIndex(List<String> data) {
	int result = data.size() - 1;
	for (int i = result; i >= 0; i--) {
	    String d = data.get(i);
	    if (d != null && d.trim() != null) {
		return i;
	    }
	}
	return result;
    }

}
