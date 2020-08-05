package GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Beth
 */
class X_Adapter extends WindowAdapter
{
	private Notepad n;

	public X_Adapter(Notepad n)
	{
		this.n = n;
	}
	public void windowClosing(WindowEvent e)
	{
		n.exit();
	}// end windowClosing
}//
