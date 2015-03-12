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
		this.Положение.add(0, 4, 0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		double c = Math.cos((double)(Время++) / 15.0 );
		double s = Math.sin((double)(Время++) / 10.0 );
		Блок = Мир.getBlockAt(Положение);
		if(Материал != null)Блок.setType(Материал);
		Положение.add(s*2, 0, c*2);
		Блок = Мир.getBlockAt(Положение);
		Материал = Блок.getType();
		Блок.setType(Material.SAND);
		Vector Вектор = Положение.getDirection();//(new Vector()).getRandom();
		
		//Arrow Стрела = 
				Мир.spawnArrow(Положение, Вектор , 0.6f , 13.0f);
		//Стрела.eject()
		//Мир.addEntity(Стрела);
//		loc.setY(loc.getY() + 5);
//	 loc.distance(o);
//	b.setType(Material.SAND);
	}

}
