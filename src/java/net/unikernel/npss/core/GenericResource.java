package net.unikernel.npss.core;

import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

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
	
	@GET
	@Path("{task}/{factory}/{paramString}")
	public String getValue(	@PathParam("task") String task,
							@PathParam("factory") String factory,
							@PathParam("paramString") String paramString)
	{
		return "This feature is not implemented yet. {" + task + ", " + factory + ", " + paramString + "}";
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
	 * PUT method for updating or creating an instance of GenericResource
	 * @param content representation for the resource
	 * @return an HTTP response with content of the updated or created resource.
	 */
	@PUT
	@Consumes("application/xml")
	public void putXml(String content)
	{
	}
}
