package com.lengyelzsolt.mmigrate;

import java.util.HashMap;
import java.util.Properties;

/**
 * 
 * @author lzsolt
 *
 */
public class PropertiesMap extends HashMap<String, Properties>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5129006132529200826L;

	@Override
	public Properties get(Object key) {
		
		if(!this.containsKey(key)){
			this.put(key.toString(), new Properties());
		}
		
		return super.get(key);
	}
		
}
