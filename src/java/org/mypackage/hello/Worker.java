/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mypackage.hello;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
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
		String user = "crawler";
		char[] pass = "J7vVBYCiGTjcnhN6Qe".toCharArray();
		try
		{
			Mongo m = new Mongo("localhost");
			DB db = m.getDB("npss");
			boolean auth = db.authenticate(user, pass);
			if (auth)
			{
				data+="<br />good";
			}
			else
			{
				data+="<br />bad";
			}
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
