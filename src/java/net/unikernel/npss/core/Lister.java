/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unikernel.npss.core;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeSet;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import net.unikernel.npss.model.PMP;
import net.unikernel.npss.model.PMP.BadLoginException;

/**
 *
 * @author uko
 */
@Stateless
@Path("/list")
public class Lister
{
	
	@EJB
	private PMP mongodbAnt;
	public Lister()
	{
	}
	@GET
	@Path("{intervel}")
	@Produces("text/html")
	public String list(@PathParam("intervel") String interval) throws BadLoginException
	{
		String result= "";
		result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		result+="\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";
		result+="\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
		result+="\n\t<head>";
		try
		{
			result+="\n\t\t<meta http-equiv=\"refresh\" content=\""+Integer.parseInt(interval)+"\">";
		}
		catch(NumberFormatException e)
		{	
		}
		result+="\n\t\t<title>Listing</title>";
		result+="\n\t</head>";
		result+="\n\t<body>";
		DecimalFormat format = new DecimalFormat("#0.00");
		result+="\n\t\t<ul>";
		for (Map.Entry<String,TreeSet<String>> i: mongodbAnt.getStructure().entrySet())
		{
			result += "\n\t\t\t<li>"+i.getKey();
			result += "\n\t\t\t\t<ul>";
			for(String j : i.getValue())
			{
				result += "\n\t\t\t\t\t<li>"+ j + " " + format.format(mongodbAnt.getSize(i.getKey(), j, PMP.SizeType.DATA_SIZE, PMP.SizeUnit.KILOBYTE))+" KB</li>";
			}
			result += "\n\t\t\t\t</ul>";
			result += "\n\t\t\t</li>";
		}
		result+="\n\t\t</ul>";
		result+="\n\t</body>";
		result+="\n</html>";
		return result;
	}
	
	@GET
	@Produces("text/html")
	public String list() throws BadLoginException
	{
		String result= "";
		result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		result+="\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";
		result+="\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
		result+="\n\t<head>";
		result+="\n\t\t<title>Listing</title>";
		result+="\n\t</head>";
		result+="\n\t<body>";
		DecimalFormat format = new DecimalFormat("#0.00");
		result+="\n\t\t<ul>";
		for (Map.Entry<String,TreeSet<String>> i: mongodbAnt.getStructure().entrySet())
		{
			result += "\n\t\t\t<li>"+i.getKey();
			result += "\n\t\t\t\t<ul>";
			for(String j : i.getValue())
			{
				result += "\n\t\t\t\t\t<li>"+ j + " " + format.format(mongodbAnt.getSize(i.getKey(), j, PMP.SizeType.DATA_SIZE, PMP.SizeUnit.KILOBYTE))+" KB</li>";
			}
			result += "\n\t\t\t\t</ul>";
			result += "\n\t\t\t</li>";
		}
		result+="\n\t\t</ul>";
		result+="\n\t</body>";
		result+="\n</html>";
		return result;
	}
}
