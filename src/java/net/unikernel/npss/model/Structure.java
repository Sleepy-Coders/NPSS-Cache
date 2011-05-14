/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unikernel.npss.model;

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.bson.types.ObjectId;

/**
 *
 * @author uko
 */
@Singleton
@Startup
public class Structure
{
	private static ObjectId id;
	private static LinkedHashMap<String,LinkedHashMap<String,ArrayList<String>>> tasks;
	/**
	 * @return the tasks
	 */
	static LinkedHashMap<String,LinkedHashMap<String,ArrayList<String>>> getStructure()
	{
		return tasks;
	}
	public Set<String> getTasks()
	{
		return tasks.keySet();		
	}
	public Set<String> getFactories(String task)
	{
		try
		{
			return tasks.get(task).keySet();
		}
		catch(Exception e)
		{
			return null;
		}
	}
	public ArrayList<String> getParameters(String task, String factory)
	{
		try
		{
			return tasks.get(task).get(factory);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	public boolean dropFactory(String task, String factory, String checkPhrase)
	{
		if (checkPhrase.equals("YES DO AS I SAID"))
		{
			PMP.getDb().getCollection(task+"."+factory).drop();
			tasks.get(task).remove(factory);
			return true;
		}
		else
		{
			return false;
		}
	}
	public Structure()
	{
		DBObject jsonCollection = PMP.getDb().getCollection("structure").findOne();
		id=(ObjectId) jsonCollection.removeField("_id");
		for(Map.Entry<String,DBObject> i : (Set<Map.Entry<String,DBObject>>)jsonCollection.toMap().entrySet())
		{
			tasks.put(i.getKey(), (LinkedHashMap) i.getValue().toMap());
		}
		
	}
}
