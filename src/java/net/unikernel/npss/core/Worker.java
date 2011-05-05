/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.unikernel.npss.core;

import net.unikernel.npss.model.PMP;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author uko
 */
public class Worker
{
	private String data;
	public Worker()
	{
		data="";
//		String user = "crawler";
//		char[] pass = "J7vVBYCiGTjcnhN6Qe".toCharArray();
		try
		{
//			Mongo m = new Mongo("localhost");
//			DB db = m.getDB("npss");
//			boolean auth = db.authenticate(user, pass);
//			if (auth)
//			{
//				data+="<br />good";
//			}
//			else
//			{
//				data+="<br />bad";
//			}
//			m.close();
			
			Map<String, Double> testMap = new HashMap<String, Double>();
			testMap.put("x", 123.124);
			testMap.put("y", 124.4632);
			testMap.put("z", 412.235);
			
			PMP mongodbAnt = new PMP();
			String taskName = "firstTestTask";
			String factoryName = "firstTestFactory";

			data += "<br/>mongodbAnt: setting data...";
			mongodbAnt.setValue(taskName, factoryName, testMap, 100.500);
			data += "<br/>mongodbAnt: data was set";
			data += "<br/>mongodbAnt: getting data...";
			Double value = mongodbAnt.getValue(taskName, factoryName, testMap);
			data += "<br/>mongodbAnt: data was extracted from MongoDB";
			data += "<br/>Task: " + taskName;
			data += "<br/>Factory: " + factoryName;
			data += "<br/>Parameters: x => " + testMap.get("x");
			data += " y => " + testMap.get("y");
			data += " z => " + testMap.get("z");
			data += "<br/>Value: " + value;
			
//			DBCollection coll = db.getCollection("testCollection");
//			Set<String> colls = db.getCollectionNames();
//			for (String s : colls)
//			{
//				data+="<br />"+s;
//			}
//			BasicDBObject doc = new BasicDBObject();
//
//			doc.put("name", "MongoDB");
//			doc.put("type", "database");
//			doc.put("count", 1);
//
//			BasicDBObject info = new BasicDBObject();
//
//			info.put("x", 203);
//			info.put("y", 102);
//
//			doc.put("info", info);
//
//			coll.insert(doc);
//			for (int i = 0; i < 100; i++)
//			{
//				coll.insert(new BasicDBObject().append("i", i));
//			}
//			data+="<br />"+coll.getCount();
//			DBCursor cur = coll.find();
//
//        while(cur.hasNext()) {
//			DBObject one=cur.next();
//			data+="<br />"+one;
//        }
//
		}
		catch (UnknownHostException ex)
		{
			System.out.print("UnknownHOST");
		}
		catch (MongoException ex)
		{
			System.out.print("MongoError");
		}
		catch(Exception e)
		{
			data += "<br/>Exception was handled: " + e.getMessage();
		}
	}
	/**
	 * @return the name
	 */ public String getData()
	{
		return data;
	}
	/**
	 * @param name the name to set
	 */ 
//	 public void setData(String data)
//	{
//		this.data = data;
//	}
}
