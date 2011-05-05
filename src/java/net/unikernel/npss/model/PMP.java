package net.unikernel.npss.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * PMP = PMP Mongo Preprocessor
 * @author uko
 */
@Singleton
@Startup
public class PMP
{
	private DB db;
	/**
	 * @return the db
	 */
	public DB getDb()
	{
		return db;
	}
	
	static public class CombiKey
	{
		public String task;
		public String factory;
		public Map<String, Double> parameters;
		public CombiKey()
		{
		}
	}

	public PMP() throws UnknownHostException, BadLoginException
	{
		String user = "crawler";
		char[] pass = "J7vVBYCiGTjcnhN6Qe".toCharArray();
		db = new Mongo("maximator.uar.net").getDB("npss");
		if (!db.authenticate(user, pass))
		{
			throw new BadLoginException();
		}
	}
	public Double getValue(String task, String factory, Map<String, Double> parameters) throws UnknownHostException
	{
		DBCursor cursor = db.getCollection(task + "." + factory).find(new BasicDBObject("parameters", new BasicDBObject(parameters)));
		if (cursor.hasNext())
		{
			return (Double) cursor.next().get("value");
		}
		else
		{
			return null;
		}
	}
	public boolean setValue(String task, String factory, Map<String, Double> parameters, Double value) throws UnknownHostException
	{
		DBCollection collection = db.getCollection(task + "." + factory);
		if (collection.find(new BasicDBObject("parameters", new BasicDBObject(parameters))).length() == 0)
		{
			collection.update(new BasicDBObject("parameters", new BasicDBObject(parameters)), new BasicDBObject("$set", new BasicDBObject("value", value)), true, true);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static class BadLoginException extends Exception
	{
		
	}
}