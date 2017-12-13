package android.org.Hurryhurry_restaurant_client.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class JSONUtil {

	
	/**
	 * getJsArray json 문자열중 키값에 해당하는 JSONArray를 반환
	 * @param json json 문자열
	 * @return JSONArray
	 */
    public static JSONArray getJsArray(String json, String key) {
    	JSONArray arr = null;
		try {
			JSONObject ja = new JSONObject(json);
			arr = ja.getJSONArray(key);
		} catch (JSONException e) {
			return null ;
		}

    	return arr;
    }
    
	/**
	 * getJsArray json 문자열기반으로 배열로 생성 (try~catch 간소화용)
	 * @param json json 문자열
	 * @return JSONObject
	 */        
    public static JSONArray getJsArray(String json)
    {
    	JSONArray	arr	= null ;
    	try {
			arr	= new JSONArray (json) ;
		} catch (JSONException e) {
			return null ;
		}
		
		return arr ;
    }
    
	/**
	 * getJsArray json 문자열중 키값에 해당하는 JSONObject를 반환
	 * @param json json 문자열
	 * @return JSONObject
	 */
    public static JSONObject getJsObject(String json, String key) {
    	JSONObject jsObj = null;
		try {
			JSONObject ja = new JSONObject(json);
			jsObj = ja.getJSONObject(key);
			
		} catch (JSONException e) {
			return null ;
		}
    	return jsObj;
    }
    
	/**
	 * getJsObject JSONObject 중 키값에 해당하는 JSONObject를 반환
	 * @param json JSONObject
	 * @return JSONObject
	 */
    public static JSONObject getJsObject(JSONObject json, String key) {
    	if( json==null ) return null;
    		
    	JSONObject jsObj = null;
		try {
			jsObj = json.getJSONObject(key);
		} catch (JSONException e) {
			return null ;
		}
    	return jsObj;
    	
    }
    
	/**
	 * getJsObject JSONArray 중 인덱스값에 해당하는 JSONObject를 반환
	 * @param json JSONArray
	 * @param idx int
	 * @return JSONObject
	 */
    public static JSONObject getJsObject(JSONArray json, int idx) {
    	if( json==null ) return null;
    	
    	JSONObject jsObj = null;
		try {
			jsObj = json.getJSONObject(idx);
		} catch (JSONException e) {
			return null ;
		}
    	return jsObj;
    }
    
	/**
	 * getJsObject JSONArray 문자열로 JSONObject생성(try~catch 간소화용)
	 * @param json JSON문자열
	 * @return JSONObject
	 */    
    public static JSONObject getJsObject(String json)
    {
    	if (json == null)			return null ;
    	
    	JSONObject jsObj = null ;
    	try {
			jsObj = new JSONObject (json) ;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null ;
		}
		
		return jsObj ;
    }
    
    
	/**
	 * getJsArray JSONObject중 키값에 해당하는 JSONArray를 반환
	 * @param json JSONObject
	 * @return JSONArray
	 */
    public static JSONArray getJsArray(JSONObject json, String key) {
    	
    	JSONArray arr = null;
		try {
			
			if( json == null )
				return null ;
			
			if( !json.has(key) )
				return null ;
			
			arr = json.getJSONArray(key);
		} catch (JSONException e) {
			return null ;
		}

    	return arr;
    }
	
    

	
    
    /***
     * JSON 객체로부터 String 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 null
     */
    public static String getJsString(JSONObject json, String key) {
    	return getJsString(json, key, null);
    }
    
    /***
     * JSON 객체로부터 String 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 defaultValue
     */
    public static String getJsString(JSONObject json, String key, String defaultValue) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getString(key);
					
				} catch (JSONException e) {
					return defaultValue;
				}
    		}
    			
    	}	
    	return defaultValue;
    }
    
    /***
     * JSON 객체로부터 String 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 defaultValue
     */
    public static String getJsString(JSONObject json, String key, String defaultValue, boolean isNumberFormat) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
    				defaultValue = json.getString(key);
				} catch (JSONException e) {
					return defaultValue;
				}
    		}
    			
    	}	
    	
    	try {
    		String sPattern = "###,###,###,###,###,##0";
    		DecimalFormat decimalformat = new DecimalFormat(sPattern);
    		defaultValue = decimalformat.format(Long.parseLong(defaultValue));
		} catch (Exception e) {
			return defaultValue;
		}
		
    	return defaultValue;
    }
    
    /**
     * JSON 객체로부터 int 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 0
     */
    public static int getJsInteger(JSONObject json, String key) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getInt(key);
					
				} catch (JSONException e) {
					return -1 ;
				}
    		}
    			
    	}	
    	return 0;
    }
    
    /**
     * JSON 객체로부터 int 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @param defaultValue
     * @return		오류가 난 경우 defaultValue
     */
    public static int getJsInteger(JSONObject json, String key, int defaultValue) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getInt(key);
					
				} catch (JSONException e) {
					return defaultValue;
				}
    		}
    			
    	}	
    	return defaultValue;
    }    
    
    /**
     * JSON 객체로부터 boolean 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 false
     */
    public static boolean getJsboolean(JSONObject json, String key) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getBoolean(key);
					
				} catch (JSONException e) {
					return false ;
				}
    		}
    			
    	}	
    	return false;
    }
    
    /**
     * JSON 객체로부터 long 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 0
     */
    public static long getJsLong(JSONObject json, String key) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getLong(key);
					
				} catch (JSONException e) {
					return -1 ;
				}
    		}
    			
    	}	
    	return 0;
    }
    
    /**
     * JSON 객체로부터 long 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @param defaultValue
     * @return		오류가 난 경우 defaultValue
     */
    public static long getJsLong(JSONObject json, String key, int defaultValue) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getLong(key);
					
				} catch (JSONException e) {
					return defaultValue;
				}
    		}
    			
    	}	
    	return defaultValue;
    }    
    
    /**
     * JSON 객체로부터 double 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @return		오류가 난 경우 -1
     */
    public static double getJsDouble(JSONObject json, String key) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getDouble(key);
					
				} catch (JSONException e) {
					return -1.0f;
				}
    		}
    			
    	}	
    	return 0;
    }
    
    /**
     * JSON 객체로부터 double 값을 읽어온다. (키 체크와 null체크를 동반)
     * @param json
     * @param key
     * @param defaultValue
     * @return		오류가 난 경우 defaultValue
     */
    public static double getJsDouble(JSONObject json, String key, double defaultValue) {
    	if( json!=null ) {
    		if( json.has(key) ) {
    			try {
					return json.getDouble(key);
					
				} catch (JSONException e) {
					return defaultValue;
				}
    		}
    			
    	}	
    	return defaultValue;
    }    
    
	/**
	 * getHeaderJsObject JSONObject 중 헤더 JSONObject를 반환
	 * @param json JSONObject
	 * @return JSONObject
	 */
    public static JSONObject getHeaderJsObject(JSONObject json) {
    	if( json==null ) return null;
		
    	JSONObject jsObj = null;
		try {
			jsObj = json.getJSONObject("header");
		} catch (JSONException e) {
			return null ;
		}
    	return jsObj;
    }	
}
