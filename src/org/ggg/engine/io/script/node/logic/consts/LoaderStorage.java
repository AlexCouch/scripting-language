package org.ggg.engine.io.script.node.logic.consts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LoaderStorage {
	private static Map<String, Long> loaderMem = new LinkedHashMap<String, Long>();
	
	public static void setVars(String key, Long var) {
		loaderMem.put(key, var);
	}
	
	public static Set<String> getKeys() {
		return loaderMem.keySet();
	}
	
	public static Long getVar(String key) {
		return loaderMem.get(key);
	}
	
	public static String getLastKey() {
		List<Entry<String, Long>> entryList = new ArrayList<Map.Entry<String, Long>>(loaderMem.entrySet());
		Entry<String, Long> lastEntry = entryList.get(entryList.size() - 1);
		return lastEntry.getKey();
	}
}
