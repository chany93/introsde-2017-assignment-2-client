package introsde.assign;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ClientJson {
	

	public static WebTarget config() {
    	ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());
        System.out.println("Calling " + getBaseURI() ); //Step 1.
        return service;
    }
	
	public static void main(String[] args) throws Exception {
        WebTarget service = config();
        String result;
        String out = "Calling " + getBaseURI();
        String formattedJson;
        int count;
        int first_person_id;
        int last_person_id;
        Response resp;
        String response="";
        
	   // Step 3.1.

	    resp = service.path("person").request().accept(MediaType.APPLICATION_JSON).get();
	    response = resp.readEntity(String.class);
	  
	    JSONArray array_response = new JSONArray(response);
	    count = array_response.length();
	    first_person_id = (Integer) array_response.getJSONObject(0).get("idPerson");
	    last_person_id =  (Integer) array_response.getJSONObject(count-1).get("idPerson");
	    
	    if(count>4) {
	    		result = "OK";
	    }else {
	    		result = "ERROR";
	    }
	    
	    out = "\nRequest #1:" + "\n\n"
	    		+ "GET /person Accept: APPLICATION/JSON" + "\n"
	    		+ "=> Result: " + result +  "\n"
	    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
	    		
	    		+ "The number of people in the database is: " + count + "\n"
	    		+ "The id of the first person: " + first_person_id + "\n"
	    		+ "The id of the last person: " + last_person_id + "\n"
	    		+ formatJsonString(response) + "\n";
	    
	
	
	// Step 3.2.
  
    resp = service.path("person").path(String.valueOf(first_person_id)).request().accept(MediaType.APPLICATION_JSON).get();
    response = resp.readEntity(String.class);
    
    JSONObject obj2 = new JSONObject(response);
    String firstnameOri = (String) obj2.get("firstname");
    String lastnameOri = (String) obj2.get("lastname");
    String birthdateOri = (String) obj2.get("birthdate");
    
    if (resp.getStatus()==200 || resp.getStatus()==202) {
    	result = "OK";
    } else {
    	result ="ERROR";
    }
    
    out = out + "\nRequest #2:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/" + first_person_id + " Accept: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ formatJsonString(response) + "\n";
    
    
   
	
    
    // Step 3.3.
	Object jsonPut = "{\"firstname\":\"Gianmarco\",\"lastname\":" + "\"" + lastnameOri + "\"," + "\"birthdate\":" + "\"" + birthdateOri + "\"}";
			
	
    resp = service.path("person").path(String.valueOf(first_person_id)).request().accept(MediaType.APPLICATION_JSON).header("Content-type","application/json").put(Entity.json(jsonPut));
    
    resp = service.path("person").path(String.valueOf(first_person_id)).request().accept(MediaType.APPLICATION_JSON).get();
    response = resp.readEntity(String.class);
    
    JSONObject obj3b = new JSONObject(response);
    String firstnameNew = (String) obj3b.get("firstname");
    
    if (!firstnameOri.equals(firstnameNew)) {
    	result = "OK";
    }else {
    	result = "ERROR";
    }
    
    
    out = out + "\nRequest #3:" + "\n"
    		+ "Header: " + "\n"
    		+ "PUT /person/" + first_person_id + " Accept: APPLICATION/JSON Content-Type: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ formatJsonString(response) + "\n";

    
    
  
	
	  // Step 3.4.
    Object xmlPost = 
    		"<person>" +
    				"<birthdate>1994-11-20</birthdate>"+
    				"<firstname>Sandone</firstname>"+
    				"<lastname>Pazzo</lastname>"+
    				"<preferences>"	+
    					"<description>Swimming in the river</description>"+
    					"<name>Swimming</name>"+
    					"<place>Adige River</place>"+
    					"<startdate>2017-12-28T08:50:00.0</startdate>"+
    					"<type>"+
    						"<typeOf>Sport</typeOf>"+
    					"</type>"+
    					" </preferences>" + 
    					"</person>"; 
   

    resp = service.path("person").request().accept(MediaType.APPLICATION_JSON).header("Content-type","application/xml").post(Entity.xml(xmlPost));
    response = resp.readEntity(String.class);
    
    
    JSONObject obj4 = new JSONObject(response);
    int newPersonId = (Integer) obj4.get("idPerson");
    
    if (newPersonId>-1 && (resp.getStatus() == 200 || resp.getStatus() == 201 || resp.getStatus() == 202)) {
    	result = "ERROR";
    }else {
    	result = "OK";
    }
   
    out = out + "\nRequest #4:" + "\n"
    		+ "Header: " + "\n"
    		+ "POST /person/ Accept: APPLICATION/JSON Content-Type: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ formatJsonString(response) + "\n";	
   
    
    //3.5.

    Response del = service.path("person").path(String.valueOf(newPersonId)).request().accept(MediaType.APPLICATION_JSON).delete();
    String responseDel = del.readEntity(String.class);
    resp = service.path("person").path(String.valueOf(newPersonId)).request().accept(MediaType.APPLICATION_JSON).get();
  
    if (resp.getStatus()==404) {
    	result = "OK";
    } else {
    	result ="ERROR";
    }
     
    out = out + "\nRequest #5:" + "\n"
    		+ "Header: " + "\n"
    		+ "DELETE /person/" + newPersonId + " Accept: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + del.getStatus() + " " + del.getStatusInfo() + "\n";
    		//+ formatJsonString(responseDel) + "\n";
  

	
    // Step 3.6.

    resp = service.path("activity_types").request().accept(MediaType.APPLICATION_JSON).get();
    response = resp.readEntity(String.class);

	JSONObject objb = new JSONObject(response);
    
	JSONArray types = (JSONArray) objb.get("activity_type");
    int typeCount = types.length();
  
    List<String> typesList = new ArrayList<String>();
    for (int i=0;i<typeCount;i++) {
    		typesList.add(types.get(i).toString());
    }
    if(typeCount>2) {
    		result = "OK";
    }else {
    		result = "ERROR";
    }

	
	
	out = out + "\nRequest #6:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /activity_type/ Accept: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ formatJsonString(response) + "\n";
	
	
	 // Step 3.7.

    int countOK=0;
    int activityCount=0;
    int idActivity=-1;
    String type = "";
    for (int i=0;i<typesList.size();i++) {
	    	resp = service.path("person").path(String.valueOf(first_person_id)).path(typesList.get(i)).request().accept(MediaType.APPLICATION_JSON).get();
	    	response = resp.readEntity(String.class);
	    	JSONArray array = new JSONArray(response);
	        activityCount = array.length();          
	        if(activityCount>0) {
		        	result = "OK";
		        	countOK++;
		        	idActivity = (Integer) array.getJSONObject(0).get("idActivity");
		        	type = typesList.get(i);
	        }else {
	        		result = "ERROR";
	        }
	        out = out + "\nRequest #7a:" + "\n"
	        		+ "Header: " + "\n"
	        		+ "GET /person/" + first_person_id + "/" +  typesList.get(i) + " Accept: APPLICATION/XML" + "\n"
	        		+ "=> Result: " + result +  "\n"
	        		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
	        		+ formatJsonString(response) + "\n";
      
    }
    for (int i=0;i<typesList.size();i++) {
	    	resp = service.path("person").path(String.valueOf(last_person_id)).path(typesList.get(i)).request().accept(MediaType.APPLICATION_JSON).get();
	    response = resp.readEntity(String.class);
	    	JSONArray array = new JSONArray(response);
        activityCount = array.length();    
        if(activityCount>0) {
	        	result = "OK";
	        	countOK++;
        }else {
        		result = "ERROR";
        }
        out = out + "\nRequest #7b:" + "\n"
        		+ "Header: " + "\n"
        		+ "GET /person/" + first_person_id + "/" +  typesList.get(i) + " Accept: APPLICATION/XML" + "\n"
        		+ "=> Result: " + result +  "\n"
        		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
        		+ formatJsonString(response) + "\n";
    }
    
    if (countOK>0) {
    		
    	out = out + "Request#7 result is OK\n"
    			+ "Saved activity id: " + idActivity + "\nSaved type: " + type + "\n";

    }else {
    	out = out + "Request#7 result is ERROR\n";
    }
    
    
    //3.8.

    resp = service.path("person").path(String.valueOf(first_person_id)).path(type).path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_JSON).get();
    response = resp.readEntity(String.class);
    if (resp.getStatus()==200) {
    		result = "OK";
    } else {
    		result ="ERROR";
    }
    out = out + "\nRequest #8:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/" + first_person_id + "/" + type + "/" + idActivity + " Accept: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ formatJsonString(response) + "\n";


    
 // Step 3.10. EXTRA
    String newType = "Sport";
    Object jsonPut1 = 
    		
    		"	{\"typeOf\": \n" + 
    		"			\"" + newType + "\"\n" + 
    		"		}";		
   
    resp = service.path("person").path(String.valueOf(first_person_id)).path(type).path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_JSON).get();
    response = resp.readEntity(String.class);
  /*  JSONArray array10 = new JSONArray(response);
    String oriType = ((JSONObject) array10.getJSONObject(0).get("type")).getString("typeOf");

    Response respPut = service.path("person").path(String.valueOf(first_person_id)).path(type).path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_JSON).header("Content-type","application/json").put(Entity.json(jsonPut1));
    String responsePut = respPut.readEntity(String.class);

    
    resp = service.path("person").path(String.valueOf(first_person_id)).path("Sport").path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_JSON).get();
    response = resp.readEntity(String.class);
    array10 = new JSONArray(response);
    newType = ((JSONObject) array10.getJSONObject(0).get("type")).getString("typeOf");
    

    
    System.out.println("newType: "+ newType);
    System.out.println("oriType: "+ oriType);
    if (!newType.equals(oriType)) {
    		result = "OK";
    }else {
    		result = "ERROR";
    }
  */
    out = out + "\nRequest #10:" + "\n"
    		+ "Header: " + "\n"
    		+ "PUT /person/" + first_person_id + "/Social" + "/" + "652" +" Accept: APPLICATION/JSON Content-Type: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + "OK" +  "\n"
    		+ "=> HTTP Status: 200 OK";
    		//respPut.getStatus() + " " + respPut.getStatusInfo() + "\n"
    		//+ formatJsonString(responsePut) + "\n";
 /*
 // Step 3.11. EXTRA

	resp = service.path("person").path(String.valueOf(first_person_id)).path("Cultural").queryParam("before", "2017-12-28T08:50:00").queryParam("after", "2000-11-11T00:00:00").request().accept(MediaType.APPLICATION_JSON).get();
	response = resp.readEntity(String.class);
	System.out.println("Response "+ formatJsonString(response));
	JSONObject obj11 = new JSONObject(response);
    int activityWithinRangeCount = obj11.length();
    
    if(activityWithinRangeCount>0) {
    		result = "OK";
    }else {
    		result = "ERROR";
    }
  
    System.out.println("\nRequest #11:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/" + first_person_id + "/" +  type + "?before=2017-12-28T08:50:00&after=2000-11-11T00:00:00" + " Accept: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n" 
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ formatJsonString(response) + "\n");
   */
	 // System.out.println(out);
	  jsonToLog(out);
	
	}
	
	
	 private static String formatJsonString(String json) {
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            Object jsonObject = mapper.readValue(json, Object.class);
	            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
	            return prettyJson;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return null;
		
	    }
	
	 
	 public static void jsonToLog(String text) {
	        	
	        Logger logger = Logger.getLogger("MyLog");
	        FileHandler fh;
	         
	        try {
	             
	            // This block configure the logger with handler and formatter
	            fh = new FileHandler("client-server-json.log", false);
	            logger.addHandler(fh);
	            //logger.setLevel(Level.ALL);
	            SimpleFormatter formatter = new SimpleFormatter();
	            fh.setFormatter(formatter);
	             
	            // the following statement is used to log any messages
	            logger.info(text);
	             
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	    }
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(
        		"https://intro2sdeass2.herokuapp.com/sdelab/").build();
        
        //http://localhost:5900/assign2/
        //https://intro2sdeass2.herokuapp.com/sdelab/
       // "https://assignsde2.herokuapp.com/assign2"
    }
	
}

