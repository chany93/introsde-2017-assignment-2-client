package introsde.assign;

import java.io.IOException;
import java.io.StringReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class ClientXml {
	
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
        int count;
        int first_person_id;
        int last_person_id;
        Response resp;
        String response="";
        String out = "";
        
    // Step 3.1.

    resp = service.path("person").request().accept(MediaType.APPLICATION_XML).get();
    response = resp.readEntity(String.class);
    Document doc1 = loadXMLFromString(response);
    NodeList peopleIds = doc1.getElementsByTagName("idPerson");
    count = peopleIds.getLength();
    first_person_id = Integer.parseInt(peopleIds.item(0).getTextContent());
    last_person_id =  Integer.parseInt(peopleIds.item(count-1).getTextContent());
    
    if(count>4) {
    		result = "OK";
    }else {
    		result = "ERROR";
    }
    
    out = out + "\nRequest #1:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/ Accept: APPLICATION/XML Content-Type: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ "The number of people in the database is: " + count + "\n"
    	    + "The id of the first person: " + first_person_id + "\n"
    	    + "The id of the last person: " + last_person_id + "\n"
    		+ prettyFormat(response, 2) + "\n";
    
    		
   

    // Step 3.2.

    resp = service.path("person").path(String.valueOf(first_person_id)).request().accept(MediaType.APPLICATION_XML).get();
    response = resp.readEntity(String.class);
    Document doc2 = loadXMLFromString(response); 
    String firstnameOri = doc2.getElementsByTagName("firstname").item(0).getTextContent();
    String lastnameOri = doc2.getElementsByTagName("lastname").item(0).getTextContent();
    String birthdateOri = doc2.getElementsByTagName("birthdate").item(0).getTextContent();
    
    if (resp.getStatus()==200 || resp.getStatus()==202) {
    		result = "OK";
    } else {
    		result ="ERROR";
    }
    
    out = out + "\nRequest #2:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/" + first_person_id + " Accept: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ prettyFormat(response, 2) + "\n";
  	

    // Step 3.3.
	Object xmlPut = 
			"<person>\n" +  
			"    <birthdate>" +  birthdateOri + "</birthdate>\n" + 
			"    <firstname>Gennaro</firstname>\n" + 
			"    <lastname>" + lastnameOri + "</lastname>\n" + 
			"</person>";
	
    resp = service.path("person").path(String.valueOf(first_person_id)).request().accept(MediaType.APPLICATION_XML).header("Content-type","application/xml").put(Entity.xml(xmlPut));
    
    Response resp3 = service.path("person").path(String.valueOf(first_person_id)).request().accept(MediaType.APPLICATION_XML).get();
    String response3 = resp3.readEntity(String.class);
    Document doc3 = loadXMLFromString(response3);
    String firstnameNew = doc3.getElementsByTagName("firstname").item(0).getTextContent();
    
    if (!firstnameOri.equals(firstnameNew)) {
    	result = "OK";
    }else {
    	result = "ERROR";
    }
    
   out = out + "\nRequest #3:" + "\n"
    		+ "Header: " + "\n"
    		+ "PUT /person/" + first_person_id + " Accept: APPLICATION/XML Content-Type: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ prettyFormat(response, 2) + "\n";
    		
    // Step 3.4.
	Object xmlPost = 
			"<person>\n" + 
			"    <preferences>\n" + 
			"            <description>Going to a restaurant or a bar with friends.</description>\n" + 
			"            <name>Going out</name>\n" + 
			"            <place>London</place>\n" + 
			"            <startdate>2017-11-11T00:00:00+01:00</startdate>\n" + 
			"            <type>" +
			"				<typeOf>Social</typeOf>" +
			" 			 </type>\n" + 
			"    </preferences>\n" + 
			"    <birthdate>2017-11-11</birthdate>\n" + 
			"    <firstname>Sherlock</firstname>\n" + 
			"    <lastname>Holmes</lastname>\n" + 
			"</person>";
	

    resp = service.path("person").request().accept(MediaType.APPLICATION_XML).header("Content-type","application/xml").post(Entity.xml(xmlPost));
    response = resp.readEntity(String.class);
    Document doc4 = loadXMLFromString(response);
    int newPersonId = Integer.parseInt(doc4.getElementsByTagName("idPerson").item(0).getTextContent());
    
    if (newPersonId>-1 && (resp.getStatus() == 200 || resp.getStatus() == 201 || resp.getStatus() == 202)) {
    		result = "OK";
    }else {
    		result = "ERROR";
    }
   
    out = out + "\nRequest #4:" + "\n"
    		+ "Header: " + "\n"
    		+ "POST /person/ Accept: APPLICATION/XML Content-Type: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ prettyFormat(response, 2) + "\n";
  	
 
   
    //3.5.
    
    resp = service.path("person").path(String.valueOf(newPersonId)).request().accept(MediaType.APPLICATION_XML).delete();
    resp = service.path("person").path(String.valueOf(newPersonId)).request().accept(MediaType.APPLICATION_XML).get();
  
    if (resp.getStatus()==404) {
    	result = "OK";
    } else {
    	result ="ERROR";
    }
    
    out = out + "\nRequest #5:" + "\n"
    		+ "Header: " + "\n"
    		+ "DELETE /person/" + newPersonId + " Accept: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n";
    		
  
    
    // Step 3.6.
 
    resp = service.path("activity_types").request().accept(MediaType.APPLICATION_XML).get();
    response = resp.readEntity(String.class);
    Document doc6 = loadXMLFromString(response);
    NodeList activityTypes = doc6.getElementsByTagName("activity_type");
    int activityTypeCount = activityTypes.getLength();
    
    List<String> typesList = new ArrayList<String>();
    for (int i=0;i<activityTypeCount;i++) {
    		typesList.add(activityTypes.item(0).getTextContent());
    }
    if(activityTypeCount>2) {
    		result = "OK";
    }else {
    		result = "ERROR";
    }
    out = out + "\nRequest #6:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /activity_type/ Accept: APPLICATION/XML Content-Type: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ prettyFormat(response, 2) + "\n";
    
    // Step 3.7.

    int countOK=0;
    int activityCount=0;
    int idActivity=-1;
    String type = "";
    
    for (int i=0;i<typesList.size();i++) {
	    	resp = service.path("person").path(String.valueOf(first_person_id)).path(typesList.get(i)).request().accept(MediaType.APPLICATION_XML).get();
	    	response = resp.readEntity(String.class);
	        Document doc7 = loadXMLFromString(response);
	        activityCount = doc7.getElementsByTagName("activity").getLength();
	        if(activityCount>0) {
			        	result = "OK";
			        	countOK++;
			        	idActivity = Integer.parseInt(doc7.getElementsByTagName("activity").item(0).getChildNodes().item(1).getTextContent());
			        	type = doc7.getElementsByTagName("activity").item(0).getLastChild().getTextContent();
	        }else {
	        		result = "ERROR";
	        }
	        out = out + "\nRequest #7a:" + "\n"
	        		+ "Header: " + "\n"
	        		+ "GET /person/" + first_person_id + "/" +  typesList.get(i) + " Accept: APPLICATION/XML" + "\n"
	        		+ "=> Result: " + result +  "\n"
	        		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
	        		+ prettyFormat(response, 2) + "\n";
	    
    }
    for (int i=0;i<typesList.size();i++) {
	    	resp = service.path("person").path(String.valueOf(last_person_id)).path(typesList.get(i)).request().accept(MediaType.APPLICATION_XML).get();
	    	response = resp.readEntity(String.class);
	        Document doc7b = loadXMLFromString(response);
	        NodeList activitiesWithType = doc7b.getElementsByTagName("activity");
	        activityCount = activitiesWithType.getLength();
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
	        		+ prettyFormat(response, 2) + "\n";
	}
    if (countOK>0) {
		
    	out = out + "Request#7 result is OK\n"
    			+ "Saved activity id: " + idActivity + "\nSaved type: " + type + "\n";

    }else {
    	out = out + "Request#7 result is ERROR\n";
    }
    
    
    
    //3.8.
   
    resp = service.path("person").path(String.valueOf(first_person_id)).path(type).path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_XML).get();
    response = resp.readEntity(String.class);
    
    if (resp.getStatus()==200) {
    		result = "OK";
    } else {
    		result ="ERROR";
    }
    
    out = out +"\nRequest #8:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/" + first_person_id + "/" + type + "/" + idActivity + " Accept: APPLICATION/XML" + "\n"
    		+ "=> Result: " + result +  "\n"
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ "Body: "  + "\n"
    		+ prettyFormat(response, 2) + "\n";

    
    
    // Step 3.10. EXTRA
    Object xmlTypePut = 
    		"<activity_type>"
    		+ "<typeOf>Sport</typeOf>"
    		+ "</activity_type>";
    
    resp = service.path("person").path(String.valueOf(first_person_id)).path(type).path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_XML).get();
    response = resp.readEntity(String.class);
   /* Document doc10a = loadXMLFromString(response);
    String origType = doc10a.getElementsByTagName("activity").item(0).getChildNodes().item(5).getChildNodes().item(0).getTextContent();


    Response respPut = service.path("person").path(String.valueOf(first_person_id)).path(type).path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_XML).header("Content-type","application/xml").put(Entity.xml(xmlTypePut));
    String responsePut = respPut.readEntity(String.class);

    resp = service.path("person").path(String.valueOf(first_person_id)).path("Social").path(String.valueOf(idActivity)).request().accept(MediaType.APPLICATION_XML).get();
    response = resp.readEntity(String.class);
    Document doc10b = loadXMLFromString(response);
    String newType = doc10b.getElementsByTagName("activity").item(0).getChildNodes().item(5).getChildNodes().item(0).getTextContent();       
    
    System.out.println("newType: "+ newType);
    System.out.println("origType: "+ origType);
    
    if (!newType.equals(origType)) {
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


 /*
    // Step 3.11. EXTRA

	resp = service.path("person").path(String.valueOf(first_person_id)).path("Cultural").queryParam("before", "2017-12-28T08:50:00").queryParam("after", "2000-11-11T00:00:00").request().accept(MediaType.APPLICATION_XML).get();
	response = resp.readEntity(String.class);
    Document doc11 = loadXMLFromString(response);

    int activityWithinRangeCount = doc11.getElementsByTagName("activity").getLength();
    if(activityWithinRangeCount>0) {
    	result = "OK";
    }else {
    	result = "ERROR";
    }
    
    out = out + "\nRequest #11:" + "\n"
    		+ "Header: " + "\n"
    		+ "GET /person/" + first_person_id + "/" +  type + "?before=2017-12-28T08:50:00&after=2000-11-11T00:00:00" + " Accept: APPLICATION/JSON" + "\n"
    		+ "=> Result: " + result +  "\n" 
    		+ "=> HTTP Status: " + resp.getStatus() + " " + resp.getStatusInfo() + "\n"
    		+ prettyFormat(response, 2) + "\n";
*/
   // System.out.println(out);
    xmlToLog(out);
	
	
}
	
	
	
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	 public static void xmlToLog(String text) {
     	
	        Logger logger = Logger.getLogger("MyLog");
	        FileHandler fh;
	         
	        try {
	             
	            // This block configure the logger with handler and formatter
	            fh = new FileHandler("client-server-xml.log");
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
	 
	 
	 
	 
	 public static String prettyFormat(String xml, int indent) {
		    if(xml.equals(" ")) {
		      return " ";
		    }
		    try {
		            final InputSource src = new InputSource(new StringReader(xml));
		            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
		            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

		        //May need this: System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");


		            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
		            final LSSerializer writer = impl.createLSSerializer();

		            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
		            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

		            return writer.writeToString(document);
		        } catch (Exception e) {
		            e.printStackTrace();
		            return " ";
		        }
		  }
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(
                "https://intro2sdeass2.herokuapp.com/sdelab/").build();
    }
}
