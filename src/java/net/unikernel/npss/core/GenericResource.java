package net.unikernel.npss.core;

import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import net.unikernel.npss.model.PMP;
import org.json.simple.JSONValue;

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
	private PMP mongodbAnt;

	/** Creates a new instance of GenericResource */
	public GenericResource()
	{
	}

	/**
	 * Retreives value from the DB for current "key"
	 * @param task	Full task name.
	 * @param factory	Full factory name
	 * @param alteredSeriesIndex
	 * @param paramJSONString	- JSON String with factory parameters.
	 * @return Data from the DB under the specified key.
	 */
	@GET
	@Path("{TaskFullName}/{FactoryFullName}/{AlteredSeriesIndex}/{Argument}")
	@Produces("text/html")
	public String getValue(	@PathParam("TaskFullName") String task,
							@PathParam("FactoryFullName") String factory,
							@PathParam("AlteredSeriesIndex") String alteredSeriesIndex,
							@PathParam("Argument") String paramJSONString)
	{
		Map<String, String> parameters = (Map<String, String>) JSONtoMap.parse(paramJSONString);
		parameters.put("AlteredSeriesIndex", alteredSeriesIndex);
		String result = mongodbAnt.read(task, factory, parameters);
		return result;
	}
	
	/**
	 * PUT method for putting a new value into the DB
	 * @param content representation for the resource {	"Task": "value", 
	 *													"Factory": "value",
	 *													"Value": value,
	 *													"AlteredSeriesIndex": value,
	 *													"Argument": {	"param0": value, 
	 *																	"param1": value,
	 *																	...}}
	 * @return an HTTP response with some debug data.
	 */
	@PUT
	@Consumes("application/json")
	@Produces("text/plain")
	public String putValue(String content)
	{
		try
		{
		Map<String, ?> map = JSONtoMap.parse(content);
		String taskName = map.get("TaskFullName").toString();
		String factoryName = map.get("FactoryFullName").toString();
		Map<String, String> sdMap = (Map<String, String>)map.get("Argument");
		sdMap.put("AlteredSeriesIndex", String.valueOf(map.get("AlteredSeriesIndex")));
		boolean set = mongodbAnt.create(taskName, factoryName, sdMap, JSONValue.toJSONString(map.get("Value")));
		String val = mongodbAnt.read(taskName, factoryName, sdMap);
		if(set)
		{
			return "Data was set: " + JSONValue.toJSONString(map) + " => (mongodbAnt has got) " + val;
		}
		return "Data was NOT set. Under the key: " + JSONValue.toJSONString(map) + ", already sits this value: " + val;
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			return "Error: " + ex.getMessage();
		}
	}
}
