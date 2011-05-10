package net.unikernel.npss.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import net.unikernel.npss.model.PMP;
import org.json.simple.JSONValue;
import org.omg.CORBA.NameValuePair;

/**
 * REST Web Service
 *
 * @author mcangel
 */
@Stateless
@Path("/backyard")
public class GenericResource
{
	@EJB
	private NameStorageBean nameStorage;
	@EJB
	private PMP mongodbAnt;

	/** Creates a new instance of GenericResource */
	public GenericResource()
	{
	}

	/**
	 * Retrieves representation of an instance of org.mypackage.hello.GenericResource
	 * @return an instance of java.lang.String
	 */
	@GET
	@Produces("text/html")
	public String getXml()
	{
		return "<html><body><h1>Hello " + nameStorage.getName() + "!</h1></body></html>";
	}

	@GET
	@Path("{newName}")
	@Produces("text/html")
	public String setXml(@PathParam("newName") String newName)
	{
		nameStorage.setName(newName);
		return "<html><body><h1>Hello " + nameStorage.getName() + "!</h1></body></html>";
	}
	
	/**
	 * Retreives value from the DB for current "key"
	 * @param task
	 * @param factory
	 * @param paramJSONString	- JSON String with factory parameters.
	 * @return Data about getting data :).
	 */
	@GET
	@Path("{task}/{factory}/{paramString}")
	@Produces("text/html")
	public String getValue(	@PathParam("task") String task,
							@PathParam("factory") String factory,
							@PathParam("paramString") String paramJSONString)
	{
		String result = "";
		result += "parsing JSON: " + paramJSONString;
		result += "<br/>JSON parsed: " + (mongodbAnt.getValue(task, factory, (Map<String, Double>)JSONtoMap.parse(paramJSONString))).toString();
		return result;
	}
	
	@GET
	@Produces("application/json")
	public Calculation getJSON()
	{
		Calculation arr = new Calculation();
		arr.task = "LinearTask";
		arr.factory = "QFi";
		arr.paramString = "abracadabra";
		arr.value = new BigDecimal(12.41234);
		return arr;
	}

	/**
	 * PUT method for putting a new value to the DB
	 * @param content representation for the resource {	"Task": "value", 
	 *													"Factory": "value",
	 *													"Value": value,
	 *													"Parameters": {	"param0": value, 
	 *																	"param1": value,
	 *																	...}}
	 * @return an HTTP response with some check data.
	 */
	@PUT
	@Consumes("application/json")
	//@Consumes("text/plain")
	@Produces("text/plain")
	public String putValue(String content)
	{
		Map<String, ?> map = JSONtoMap.parse(content);
		String taskName = map.get("Task").toString();
		String faktoryName = map.get("Factory").toString();
		Map<String, Double> sdMap = (Map<String, Double>)map.get("Parameters");
		boolean setted = mongodbAnt.setValue(taskName, faktoryName, sdMap, (Double)map.remove("Value"));
		Double val = mongodbAnt.getValue(taskName, faktoryName, sdMap);
		if(setted)
		{
			return "Data was set: " + JSONValue.toJSONString(map) + " => (mongodbAnt has got) " + val;
		}
		return "Data was NOT set. Under key: " + JSONValue.toJSONString(map) + ", already sits this value: " + val;
	}
}
