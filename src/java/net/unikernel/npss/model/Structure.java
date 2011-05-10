/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unikernel.npss.model;

import java.util.ArrayList;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author uko
 */
@Singleton
@Startup
public class Structure
{
	private Map<String,Map<String,ArrayList<String>>> tasks;
	public Structure()
	{
		
	}
}
