package org.solq.excel.compare;

import java.util.List;
import java.util.Map;

public class CompareRemoveProcessor implements IProcessor {

    public boolean compare(String newFile, String oldFile, Map<String, Map<Integer, List<String>>> newbody, Map<String, Map<Integer, List<String>>> oldbody) {
	if (!newFile.isEmpty()) {
	    return false;
	}

	System.out.println("ÎÄ¼þÎª¿Õ : " + newFile);
	return true;
    }

}
