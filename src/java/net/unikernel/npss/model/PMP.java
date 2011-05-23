package net.unikernel.npss.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
	public enum SizeType
	{
		DATA_SIZE ("size"),
		STORAGE_SIZE ("storageSize"),
		INDEX_SIZE ("totalIndexSize");
		private final String value;
		private SizeType(String value)
		{
			this.value = value;
		}
		public String value(){return value;}
	};
	
	public enum SizeUnit
	{
		BIT (1/8),
		BYTE (1),
		KILOBYTE (1024),
		MEGABYTE (1024*1024),
		GIGABYTE (1024*1024*1024);
		private final double value;
		private SizeUnit(double value)
		{
			this.value = value;
		}
		public double value(){return value;}
	};
	
	private DB db;
	/**
	 * @return the db
	 */
	DB getDb()
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
		public CombiKey(String task, String factory, Map<String, Double> parameters)
		{
			this.task = task;
			this.factory = factory;
			this.parameters = parameters;
		}
	}

	public PMP()
	{
	}
	
	@PostConstruct
	public void init() throws MongoException, UnknownHostException, BadLoginException
	{
		String user = "crawler";
		char[] pass = "J7vVBYCiGTjcnhN6Qe".toCharArray();
		db = new Mongo("maximator.uar.net").getDB("npss");
		if (!db.authenticate(user, pass))
		{
			throw new BadLoginException();
		}
	}
	
	@PreDestroy
	public void dispose()
	{
		db.getMongo().close();
	}
	
	/**
	 * Returns value under specified key (task + factory + parameters), or null 
	 * if there is no value under this key in the DB.
	 * @param task
	 * @param factory
	 * @param parameters
	 * @return
	 * @throws MongoException 
	 */
	public Double read(String task, String factory, Map<String, Double> parameters) throws MongoException
	{
		DBCursor cursor = db.getCollection("st."+task + "." + factory).find(new BasicDBObject("parameters", new BasicDBObject(parameters)));
		if (cursor.hasNext())
		{
			return (Double) cursor.next().get("value");
		}
		else
		{
			return null;
		}
	}
	public Double read(CombiKey key) throws MongoException
	{
		DBCursor cursor = db.getCollection("st."+key.task + "." + key.factory).find(new BasicDBObject("parameters", new BasicDBObject(key.parameters)));
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
	 * @param value			Value to delete into the DB.
	 * @return True if there are no data under this key (task+factory+parameters) and value was inserted, otherwise - returns false.
	 * @throws MongoException
	 */
	public boolean create(String task, String factory, Map<String, Double> parameters, Double value) throws MongoException
	{
		if(!db.getCollectionNames().contains("st."+task + "." + factory))
		{
			BasicDBObject index = new BasicDBObject();
			index.put("parameters", 1);
			index.put("unique", true);
			db.getCollection("st."+task + "." + factory).ensureIndex(index);
		}
		BasicDBObject insert = new BasicDBObject();
		insert.put("parameters", parameters);
		insert.put("value", value);
		db.getCollection("st."+task + "." + factory).insert(insert);
		return true;
	}
	
	public boolean create(CombiKey key, Double value) throws MongoException
	{ 
		if(!db.getCollectionNames().contains("st."+key.task + "." + key.factory))
		{
			BasicDBObject index = new BasicDBObject();
			index.put("parameters", 1);
			index.put("unique", true);
			db.getCollection("st."+key.task + "." + key.factory).ensureIndex(index);
		}
		BasicDBObject insert = new BasicDBObject();
		insert.put("parameters", key.parameters);
		insert.put("value", value);
		if(db.getCollection("st."+key.task + "." + key.factory).insert(insert).getLastError().get("err")==null)
			return true;
		else
			return false;
	}
	
	public void delete(String task, String factory, Map<String, Double> parameters) throws MongoException
	{
		db.getCollection("st."+task + "." + factory).remove(new BasicDBObject("parameters", new BasicDBObject(parameters)));
	}
	
	public void delete(CombiKey key) throws MongoException
	{
		db.getCollection("st."+key.task + "." + key.factory).remove(new BasicDBObject("parameters", new BasicDBObject(key.parameters)));
	}
	
	public void update(String task, String factory, Map<String, Double> parameters, Double value)
	{
		db.getCollection("st."+task + "." + factory).update(new BasicDBObject("parameters", new BasicDBObject(parameters)), new BasicDBObject("$set", new BasicDBObject("value", value)), false, false);
	}
	
	public void update(CombiKey key, Double value)
	{
		db.getCollection("st."+key.task + "." + key.factory).update(new BasicDBObject("parameters", new BasicDBObject(key.parameters)), new BasicDBObject("$set", new BasicDBObject("value", value)), false, false);
	}
	
	TreeMap<String,TreeSet<String>> getStructure()
	{
		TreeMap<String,TreeSet<String>> result = new TreeMap<String,TreeSet<String>>();
		for(String i :db.getCollectionNames())
		{
			String[] set = i.split(".");
			if(set[0].equals("st"))
			{
				if(!result.containsKey(set[1]))
				{
					result.put(set[1], new TreeSet<String>());
				}
				((TreeSet<String>)result.get(set[1])).add(set[2]);
			}
		}
		return result;
	}
	
	public Double getSize(String task, String factory, String sizeType)
	{
		return getSize(task, factory, SizeType.DATA_SIZE, SizeUnit.BYTE);
	}
	
	public Double getSize(String task, String factory, SizeType sizeType)
	{
		return getSize(task, factory, sizeType, SizeUnit.BYTE);
	}
	
	public Double getSize(String task, String factory, SizeUnit sizeUnit)
	{
		return getSize(task, factory, SizeType.DATA_SIZE, sizeUnit);
	}
	
	public Double getSize(String task, String factory, SizeType sizeType, SizeUnit sizeUnit)
	{
		return (Double)db.getCollection("st."+task + "." + factory).getStats().get(sizeType.value())/sizeUnit.value;
	}
	
	public static class BadLoginException extends Exception
	{
		
	}
}