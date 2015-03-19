/**
 * 
 */
package ru.znmine;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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
	float pitch;
	float yaw;
	Logger log = Logger.getLogger("Minecraft");
	/**
	 * 
	 */
	public ПотокЛетающийБлок(Location Положение) {
		// TODO Auto-generated constructor stub
		this.Положение = Положение; //пока для чистоты эксперимента не клонирую класс, понадеюсь на то что сборщик мусора умненький и определит это как замыкание  
		this.Время = 0;
		this.Мир = Положение.getWorld();
		//this.Положение.add(0, 3, 0);
		//this.Положение.setY(y);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		
//		for (Location l : signs.toArray(new Location[0])) {
//			final BlockState bs = l.getBlock().getState();
//			if (bs instanceof Sign) {
//				if (plugin.config.isClockwise) {
//					if (l.getBlock().getData() == 0xF) {
//						l.getBlock().setData((byte) 0);
		
		
	//	double c = Math.cos((double)(Время++) / 55.0 );
	//	double s = Math.sin((double)(Время++) / 50.0 );
		Блок = Мир.getBlockAt(Положение);
	//	if(Материал != null){Блок.setType(Материал);}
	//	Положение.add(s/12.0, 0, c/12.0);
	//	Блок = Мир.getBlockAt(Положение);
	//	Материал = Блок.getType();
		Блок.setType(Material.SIGN_POST);
		
		double МинимальноеРасстояние = 13.0,Расстояние;
		Player НайденИгрок = null;
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			Расстояние = Положение.distance(player.getLocation());
			if(МинимальноеРасстояние > Расстояние){МинимальноеРасстояние = Расстояние;НайденИгрок = player;}
		}

		
		if(НайденИгрок == null)return;

		//log.info(НайденИгрок.getName()+"  "+ String.valueOf(МинимальноеРасстояние));
		//@SuppressWarnings("unused")
	//	Расположение Турель = new Расположение(Положение);
		Location l = НайденИгрок.getLocation().clone().subtract(Положение);
		Vector Вектор = (new Vector(l.getX(), l.getY()+МинимальноеРасстояние * 0.35, l.getZ())).normalize();
		//log.info(Вектор.toString());
		setDirection(Вектор);
		
//		Положение.setYaw((float) yaw);
		if (Блок.getState() instanceof Sign) {
			Блок.setData((byte)(yaw));
			Sign sign = (Sign)Блок.getState();
			//sign.setData((byte)yaw*64);
			sign.update();
		}	
		
		//if()
		
		//Location testLoc = Положение.clone().add(2 * c, 5, 5 * s);
		//Vector Вектор = new Vector(0,1,0); //Положение.getDirection();//(
		//Вектор.getEpsilon()
		//Вектор.angle(testLoc.toVector());
		
		//Arrow Стрела = 
				//Мир.spawnArrow(Положение, Турель.НаправлениеНа(НайденИгрок.getLocation()) , 1.6f , 13.0f);
				Мир.spawnArrow(Положение, Вектор , 1.6f , 13.0f);
		//Стрела.eject()
		//Мир.addEntity(Стрела);
//		loc.setY(loc.getY() + 5);
//	 loc.distance(o);
//	b.setType(Material.SAND);
	}
	
	
	 /**
     * Sets the {@link #getYaw() yaw} and {@link #getPitch() pitch} to point
     * in the direction of the vector.
     */
    public Location setDirection(Vector vector) {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        final double _2PI = 2 * Math.PI;
        final double x = vector.getX();
        final double z = vector.getZ();

        if (x == 0 && z == 0) {
            pitch = vector.getY() > 0 ? -90 : 90;
            return Положение;
        }

        double theta = Math.atan2(-x, z);
        yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

        double x2 = x * x;
        double z2 = z * z;
        double xz = Math.sqrt(x2 + z2);
        pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));

        return Положение;
    }

}
