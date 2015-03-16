/**
 * 
 */
package ru.znmine;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
//import org.bukkit.entity.Arrow;
import org.bukkit.util.Vector;

/**
 * @author znsoft
 *
 */
public class ПотокЛетающийБлок implements Runnable {
	Location Положение;
	int Время;
	World Мир;// = loc.getWorld();
	Block Блок;
	Material Материал;
	/**
	 * 
	 */
	public ПотокЛетающийБлок(Location Положение) {
		// TODO Auto-generated constructor stub
		this.Положение = Положение; //пока для чистоты эксперимента не клонирую класс, понадеюсь на то что сборщик мусора умненький и определит это как замыкание  
		this.Время = 0;
		this.Мир = Положение.getWorld();
		this.Положение.add(0, 3, 0);
		//this.Положение.setY(y);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		
		
		double c = Math.cos((double)(Время++) / 55.0 );
		double s = Math.sin((double)(Время++) / 50.0 );
		Блок = Мир.getBlockAt(Положение);
		if(Материал != null){Блок.setType(Материал);}
		Положение.add(s/12.0, 0, c/12.0);
		Блок = Мир.getBlockAt(Положение);
		Материал = Блок.getType();
		//if()
		
			//Блок.setType(Material.SAND);
		//Location testLoc = Положение.clone().add(2 * c, 5, 5 * s);
		Vector Вектор = new Vector(0,1,0); //Положение.getDirection();//(
		//Вектор.getEpsilon()
		//Вектор.angle(testLoc.toVector());
		
		//Arrow Стрела = 
				Мир.spawnArrow(Положение, Вектор , 1.6f , 13.0f);
		//Стрела.eject()
		//Мир.addEntity(Стрела);
//		loc.setY(loc.getY() + 5);
//	 loc.distance(o);
//	b.setType(Material.SAND);
	}

}
