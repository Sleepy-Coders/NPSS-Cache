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
import net.unikernel.npss.controller.PMP;
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
	@Path("{TaskName}/{FactoryName}/{AlteredSeriesIndex}/{SurfaceFileHash}/{Argument}")
	@Produces("text/html")
	public String getValue(	@PathParam("TaskName") String task,
							@PathParam("FactoryName") String factory,
							@PathParam("AlteredSeriesIndex") String alteredSeriesIndex,
							@PathParam("SurfaceFileHash") String fileHash,
							@PathParam("Argument") String paramJSONString)
	{
		try
		{
			Map<String, String> parameters;
			if(paramJSONString.charAt(0) == '{')
			{//if it's a map
				parameters = (Map<String, String>) JSONtoMap.parse(paramJSONString);
			}
			else
			{//if it's some value possibly numeric
				parameters = new java.util.LinkedHashMap();
				parameters.put("#", paramJSONString);
			}
			parameters.put("AlteredSeriesIndex", alteredSeriesIndex);
			parameters.put("SurfaceFileHash", fileHash);
			String result = mongodbAnt.read(task, factory, parameters);
			return result;
		}
		catch(Exception ex)
		{
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			StackTraceElement[] stack = ex.getStackTrace();
			for(int i = 0; i < stack.length; i++)
			{
				System.err.println(stack[i]);
			}
		}
		return null;
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
			String taskName = map.get("TaskName").toString();
			String factoryName = map.get("FactoryName").toString();
			Map<String, String> sdMap;
			if(map.get("Argument") instanceof Map)
			{
				sdMap = (Map<String, String>) map.get("Argument");
			}
			else
			{
				sdMap = new java.util.LinkedHashMap();
				sdMap.put("#", String.valueOf(map.get("Argument")));
			}
			sdMap.put("AlteredSeriesIndex", String.valueOf(map.get("AlteredSeriesIndex")));
			sdMap.put("SurfaceFileHash", (String)map.get("SurfaceFileHash"));
			
			boolean set = mongodbAnt.create(taskName, factoryName, sdMap, JSONValue.toJSONString(map.get("Value")));
			if (set)
			{
				return "Data was set.";
			}
			return "Data was NOT set.";
		}
		catch (Exception ex)
		{
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			StackTraceElement[] stack = ex.getStackTrace();
			for(int i = 0; i < stack.length; i++)
			{
				System.err.println(stack[i]);
			}
			return "Error: " + ex.getMessage();
		}
	}
}
