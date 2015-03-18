/**
 * 
 */
package ru.znmine;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * @author znsoft
 *
 */
public class Расположение {

	private Location Положение;
	
	public Расположение(Location l) {
		УстановитьПоложение(l);
	}


	public Location ПолучитьПоложение() {
		return Положение;
	}


	public void УстановитьПоложение(Location положение) {
		Положение = положение;
	}
	
	public Vector НаправлениеНа(Location Объект){
		Location l = Положение.clone().subtract(Объект);
		return new Vector(l.getX(),l.getY(),l.getZ());
	}
	

}
