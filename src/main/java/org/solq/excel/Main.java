package org.solq.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.solq.excel.compare.CompareBlockProcessor;
import org.solq.excel.compare.CompareCreateProcessor;
import org.solq.excel.compare.CompareRemoveProcessor;
import org.solq.excel.compare.IProcessor;

/**
 * @author solq
 **/
public class Main {
    private final static List<IProcessor> PROCESSORS = new ArrayList<IProcessor>();

    static {
	PROCESSORS.add(new CompareCreateProcessor());
	PROCESSORS.add(new CompareRemoveProcessor());
	PROCESSORS.add(new CompareBlockProcessor());
    }

    public static void main(String[] args) {
	String newFile = args[0];
	String oldFile = args[1];

	Map<String, Map<Integer, List<String>>> newbody = ExcelUtils.readExcelData(newFile);
	Map<String, Map<Integer, List<String>>> oldbody = ExcelUtils.readExcelData(oldFile);

	// 对比策略

	// 完全新增
	// 完全删除
	// 连续块显示比较 { 中间插入,中间删除，连续相同 }
	for (IProcessor p : PROCESSORS) {
	    if (p.compare(newFile, oldFile, newbody, oldbody)) {
		break;
	    }
	}
    }

}
