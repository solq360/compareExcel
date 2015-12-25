package org.solq.excel;

import java.util.List;

public class FilterInfo {
    private int newIndex;
    private List<String> newData;

    private int oldIndex;
    private List<String> oldData;

    public static FilterInfo of(int newIndex, List<String> newData, int oldIndex, List<String> oldData) {
	FilterInfo reuslt = new FilterInfo();
	reuslt.newData = newData;
	reuslt.oldData = oldData;
	reuslt.oldIndex = oldIndex;
	reuslt.newIndex = newIndex;
	return reuslt;
    }

    public String toNewData() {
	if (newData == null) {
	    return "";
	}
	StringBuilder sb = new StringBuilder();
	for (String n : newData) {
	    sb.append("\t");
	    sb.append(n);
	}
	return sb.toString();
    }

    public String toOldData() {
	if (oldData == null) {
	    return "";
	}
	StringBuilder sb = new StringBuilder();
	for (String n : oldData) {
	    sb.append("\t");
	    sb.append(n);
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