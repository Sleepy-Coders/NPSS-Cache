package net.unikernel.npss.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
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
	private static DB db;
	/**
	 * @return the db
	 */
	static DB getDb()
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

	public PMP() throws MongoException, UnknownHostException, BadLoginException
	{
		String user = "crawler";
		char[] pass = "J7vVBYCiGTjcnhN6Qe".toCharArray();
		db = new Mongo("maximator.uar.net").getDB("npss");
		if (!db.authenticate(user, pass))
		{
			throw new BadLoginException();
		}
	}
	public Double getValue(String task, String factory, Map<String, Double> parameters) throws MongoException
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
	/**
	 * Adds value to the DB if there is no any value under this key and returns true, otherwise returns false.
	 * @param task			Name of the task.
	 * @param factory		Name of the factory.
	 * @param parameters	Mapped parameters collection.
	 * @param value			Value to insert into the DB.
	 * @return True if there are no data under this key (task+factory+parameters) and value was inserted, otherwise - returns false.
	 * @throws MongoException
	 */
	public boolean setValue(String task, String factory, Map<String, Double> parameters, Double value) throws MongoException
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