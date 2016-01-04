package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DatabaseConnector {
	
	public DatabaseConnector(){
		
	}
	
	/**
	 * 
	 * @param test_suite_name
	 * @return
	 * @throws Throwable
	 */
	public List<String> testSuiteRequest(String test_suite_name) throws Throwable{
		List<String> test_namesList = new ArrayList<String>();
		String urlString =  KeyWords.DATABASE_URL.toString();
		String testsuiteUrlString = KeyWords.TESTSUITES_URL.toString();
		URL testsuiteUrl = new URL(urlString+testsuiteUrlString);
		HttpURLConnection con = (HttpURLConnection) testsuiteUrl.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");
		
		JSONObject searchPropertyJsonObject = new JSONObject();
		searchPropertyJsonObject.put("end_timestamp", "2015-01-11 01:00:00");
		searchPropertyJsonObject.put("start_timestamp", "2015-01-01 01:00:00");
		searchPropertyJsonObject.put("test_suite_name", test_suite_name);
		JSONObject searchJsonObject = new JSONObject();
		searchJsonObject.put("search", searchPropertyJsonObject);
		
		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(searchJsonObject.toString());
		wr.flush();
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 
		if(HttpResult == HttpURLConnection.HTTP_OK){
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }  

		    br.close();  
		    
		    JSONTokener jsonTokener = new JSONTokener(sb.toString());
		    JSONObject outputJsonObject = new JSONObject(jsonTokener);
		    JSONArray valueJsonArray = outputJsonObject.getJSONArray("values");
		    if(valueJsonArray.length() == 0){
		    	return test_namesList;
		    }else{
		    	for(int i = 0; i < valueJsonArray.length(); i ++){
		    		test_namesList.add(valueJsonArray.getString(i));
		    	}
		    	return test_namesList;
		    }
		    
//		    int spacesToIndentEachLevel = 2;
//		    System.out.println(outputJsonObject.toString(spacesToIndentEachLevel));
//		    System.out.println("-------------------");
//		    System.out.println("Field: "+fieldString);
		    
		}else{
		    System.out.println(con.getResponseMessage());
		    return test_namesList;
		}
	}
	
	/**
	 * 
	 * @param mpi_name
	 * @param mpi_version
	 * @return
	 * @throws Throwable
	 */
	public String installRuntimeRequest(String mpi_name, String mpi_version) throws Throwable{
		String avg_runtimeString = "";
		
		String urlString =  KeyWords.DATABASE_URL.toString();
		String testsuiteUrlString = KeyWords.RUNTIME_URL.toString();
		URL testsuiteUrl = new URL(urlString+testsuiteUrlString);
		HttpURLConnection con = (HttpURLConnection) testsuiteUrl.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");
		
		JSONArray phasesJsonArray = new JSONArray();
		phasesJsonArray.put("install");
		
		JSONObject searchPropertyJsonObject = new JSONObject();
		searchPropertyJsonObject.put("end_timestamp", "2015-01-11 01:00:00");
		searchPropertyJsonObject.put("start_timestamp", "2015-01-01 01:00:00");
		searchPropertyJsonObject.put("mpi_name", mpi_name);
//		searchPropertyJsonObject.put("mpi_version", mpi_version);
		
		JSONObject searchJsonObject = new JSONObject();
		searchJsonObject.put("phases", phasesJsonArray);
		searchJsonObject.put("search", searchPropertyJsonObject);
		
		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(searchJsonObject.toString());
		wr.flush();
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 
		if(HttpResult == HttpURLConnection.HTTP_OK){
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }  

		    br.close();  
		    
		    JSONTokener jsonTokener = new JSONTokener(sb.toString());
		    JSONObject outputJsonObject = new JSONObject(jsonTokener);
		    JSONArray runtimeJsonArray = outputJsonObject.getJSONArray("values");
		    if(runtimeJsonArray.isNull(1)){
		    	return "null";
		    }else {
				return runtimeJsonArray.getString(1);
			}

		}else{
		    System.out.println(con.getResponseMessage());
		    return avg_runtimeString;
		}
	}
	
	/**
	 * 
	 * @param test_suite_name
	 * @return
	 * @throws Throwable
	 */
	public String test_buildRuntimeRequest(String test_suite_name)throws Throwable{
		String avg_runtimeString = "";
		
		String urlString =  KeyWords.DATABASE_URL.toString();
		String testsuiteUrlString = KeyWords.RUNTIME_URL.toString();
		URL testsuiteUrl = new URL(urlString+testsuiteUrlString);
		HttpURLConnection con = (HttpURLConnection) testsuiteUrl.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");
		
		JSONArray phasesJsonArray = new JSONArray();
		phasesJsonArray.put("test_build");
		
		JSONObject searchPropertyJsonObject = new JSONObject();
		searchPropertyJsonObject.put("end_timestamp", "2015-01-11 01:00:00");
		searchPropertyJsonObject.put("start_timestamp", "2015-01-01 01:00:00");
		searchPropertyJsonObject.put("test_suite_name", test_suite_name);
		
		JSONObject searchJsonObject = new JSONObject();
		searchJsonObject.put("phases", phasesJsonArray);
		searchJsonObject.put("search", searchPropertyJsonObject);
		
		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(searchJsonObject.toString());
		wr.flush();
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 
		if(HttpResult == HttpURLConnection.HTTP_OK){
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }  

		    br.close();  
		    
		    JSONTokener jsonTokener = new JSONTokener(sb.toString());
		    JSONObject outputJsonObject = new JSONObject(jsonTokener);
		    JSONArray runtimeJsonArray = outputJsonObject.getJSONArray("values");
		    
		    if(runtimeJsonArray.isNull(1)){
		    	return "null";
		    }else {
				return runtimeJsonArray.getString(1);
			}
		    
		}else{
		    System.out.println(con.getResponseMessage());
		    return avg_runtimeString;
		}
	}
	
	/**
	 * 
	 * @param test_name
	 * @return
	 * @throws Throwable
	 */
	public String test_runRuntimeRequest(String test_name)throws Throwable{
		String avg_runtimeString = "";
		
		String urlString =  KeyWords.DATABASE_URL.toString();
		String testsuiteUrlString = KeyWords.RUNTIME_URL.toString();
		URL testsuiteUrl = new URL(urlString+testsuiteUrlString);
		HttpURLConnection con = (HttpURLConnection) testsuiteUrl.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");
		
		JSONArray phasesJsonArray = new JSONArray();
		phasesJsonArray.put("test_run");
		
		JSONObject searchPropertyJsonObject = new JSONObject();
		searchPropertyJsonObject.put("end_timestamp", "2015-01-11 01:00:00");
		searchPropertyJsonObject.put("start_timestamp", "2015-01-01 01:00:00");
		searchPropertyJsonObject.put("test_name", test_name);
		
		JSONObject searchJsonObject = new JSONObject();
		searchJsonObject.put("phases", phasesJsonArray);
		searchJsonObject.put("search", searchPropertyJsonObject);
		
		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(searchJsonObject.toString());
		wr.flush();
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 
		if(HttpResult == HttpURLConnection.HTTP_OK){
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }  

		    br.close();  
		    
		    JSONTokener jsonTokener = new JSONTokener(sb.toString());
		    JSONObject outputJsonObject = new JSONObject(jsonTokener);
		    JSONArray runtimeJsonArray = outputJsonObject.getJSONArray("values");

		    if(runtimeJsonArray.isNull(1)){
		    	return "null";
		    }else {
				return runtimeJsonArray.getString(1);
			}
		}else{
		    System.out.println(con.getResponseMessage());
		    return avg_runtimeString;
		}
	}
}
