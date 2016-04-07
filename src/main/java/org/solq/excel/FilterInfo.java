package org.solq.excel;

import java.util.Collections;
import java.util.List;

public class FilterInfo {
    private int newIndex;
    private List<String> newData;

    private int oldIndex;
    private List<String> oldData;

    @SuppressWarnings("unchecked")
    public static FilterInfo of(int newIndex, List<String> newData, int oldIndex, List<String> oldData) {
	FilterInfo reuslt = new FilterInfo();
	if (newData == null) {
	    newData = Collections.EMPTY_LIST;
	}
	if (oldData == null) {
	    oldData = Collections.EMPTY_LIST;
	}
	reuslt.newData = newData;
	reuslt.oldData = oldData;
	reuslt.oldIndex = oldIndex;
	reuslt.newIndex = newIndex;
	return reuslt;
    }

    public String toNewData() {
	return formatInfo(newData, oldData);

    }

    public String toOldData() {
	return formatInfo(oldData, newData);
    }

    private String formatInfo(List<String> d1, List<String> d2) {
	if (d1 == null) {
	    return "";
	}
	StringBuilder sb = new StringBuilder();
	final int d2Len = d2.size() - 1;
	for (int i = 0; i < d1.size(); i++) {
	    String n = d1.get(i);
	    sb.append("\t");
	    boolean addFlag = false;
	    if (d2Len > i && n != null) {
		String n2 = d2.get(i);
		if (n2 != null && !n2.equals(n)) {
		    addFlag = true;
		}
	    }

	    if (addFlag) {
		sb.append("********* ");
		sb.append(n);
		sb.append(" *********");
	    } else {
		sb.append(n);
	    }
	}
	return sb.toString();
    }

    // getter

    public int getNewIndex() {
	return newIndex + 1;
    }

    public List<String> getNewData() {
	return newData;
    }

    public int getOldIndex() {
	return oldIndex + 1;
    }

    public List<String> getOldData() {
	return oldData;
    }

}