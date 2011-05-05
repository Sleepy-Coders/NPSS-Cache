/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unikernel.npss.core;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mcangel
 */
@XmlRootElement
public class Calculation
{
	public String task;
	public String factory;
	public String paramString;
	public java.math.BigDecimal value;
	
	public Calculation()
	{
	}
}
