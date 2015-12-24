package org.solq.excel.compare;

import java.util.List;
import java.util.Map;

public class CompareCreateProcessor implements IProcessor {

	public boolean compare(String newFile, String oldFile,
			Map<String, Map<Integer, List<String>>> newbody,
			Map<String, Map<Integer, List<String>>> oldbody) {
		if (!oldbody.isEmpty()) {
			return false;
		}

		System.out.println("新添加文件 : " + newFile);
		return true;
	}

}
