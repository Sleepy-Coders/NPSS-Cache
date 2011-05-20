package net.unikernel.npss.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

/**
 * Recursive JSON to Map parser.
 * @author mcangel
 */
public class JSONtoMap
{
	private static ContainerFactory containerFactory = new ContainerFactory()
	{
		@Override
		public Map createObjectContainer()
		{
			return new LinkedHashMap();
		}

		@Override
		public List creatArrayContainer()
		{
			return new LinkedList();
		}
	};

	public static Map<String, ?> parse(String JSONString)
	{
		try
		{
			Map<String, ?> map = (Map)(new JSONParser()).parse(JSONString, containerFactory);
			return map;
		}
		catch (org.json.simple.parser.ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}