package org.solq.excel.compare;

import java.util.List;
import java.util.Map;

public interface IProcessor {
	public boolean compare(String newFile,String oldFile ,Map<String, Map<Integer, List<String>>> newbody,
			Map<String, Map<Integer, List<String>>> oldbody);
}
