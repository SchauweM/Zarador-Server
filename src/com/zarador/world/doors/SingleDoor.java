package com.zarador.world.doors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.zarador.model.GameObject;
import com.zarador.model.Position;
import com.zarador.world.entity.impl.player.Player;

/**
 * 
 * @author Killamess
 * Basic door manipulation
 *
 */
public final class SingleDoor {

	private static SingleDoor singleton = null;

	@SuppressWarnings("unused")
	private List<SingleDoor> doors = new ArrayList<SingleDoor>();

	@SuppressWarnings("unused")
	private File doorFile;
	
	public static SingleDoor getSingleton() {
		if (singleton == null) {
			singleton = new SingleDoor("./data/def/doors/single_doors.txt");
		}
		return singleton;
	}

	private SingleDoor(String file){
		doorFile = new File(file);  
	}
	
	private SingleDoor(int door, int x, int y, int z, int face, int type, int open) {
		this.doorId = door;
		this.originalId = door;
		this.doorX = x;
		this.doorY = y;
		this.originalX = x;
		this.originalY = y;
		this.doorZ = z;
		this.originalFace = face;
		this.currentFace = face;
		this.type = type;
		this.open = open;
	}
	
	public static boolean isDoor(Player player, GameObject gameObject) {
		switch (gameObject.getId()) {
		case 28589:
		case 28690:
		case 28691:
			boolean side_door = !gameObject.getPosition().sameAs(new Position(2884, 3440))
								&& !gameObject.getPosition().sameAs(new Position(2884, 3438))
								&& !gameObject.getPosition().sameAs(new Position(2893, 3421))
								&& !gameObject.getPosition().sameAs(new Position(2886, 3420));
			moveThroughDoor(player, gameObject, side_door);
			return true;
		case 2266:
			moveThroughDoor(player, gameObject, false);
			return true;
		default:
			if (gameObject.getName() != null && gameObject.getName().toLowerCase().contains("door")) {
				moveThroughDoor(player, gameObject, false);
				return true;
			}
			return false;
		}
	}
	
	public static void moveThroughDoor(final Player player, final GameObject gameObject, boolean sideDoor) {		
		final int[] coords = new int[2];
		switch (gameObject.getRotation()) {
		case 0:
			if (player.getPosition().getX() >= gameObject.getPosition().getX()) {
				coords[0] = -1;
			} else {
				coords[0] = 0;
			}
			break;
		case 2:
			if (player.getPosition().getX() <= gameObject.getPosition().getX()) {
				coords[0] = 1;
			} else {
				coords[0] = 0;
			}
			break;
		case 1:
			if (player.getPosition().getY() <= gameObject.getPosition().getY()) {
				coords[1] = 1;
			} else {
				coords[1] = 0;
			}
			break;
		case 3:
			if (player.getPosition().getY() >= gameObject.getPosition().getY()) {
				coords[1] = -1;
			} else {
				coords[1] = 0;
			}
			break;
		default:
			if (player.getStaffRights().isDeveloper(player)) {
				player.getPacketSender().sendMessage("@red@Unhandled door rotation: " + gameObject.getRotation());
			}
			break;
		}
		player.moveTo(new Position(gameObject.getPosition().getX() + coords[0],
				gameObject.getPosition().getY() + coords[1], player.getPosition().getZ()));

	}
	
	@SuppressWarnings("unused")
	private SingleDoor getDoor(int id, int x, int y, int z) {
		/*for (SingleDoor d : doors) {
			if (d.doorId == id) {
				if (d.doorX == x && d.doorY == y && d.doorZ == z) {
					return d;
				}
			}
		}*/
		return null;
	}
	
	public boolean handleDoor(int id, int x, int y, int z) {

		/*SingleDoor d = getDoor(id, x, y, z);
		
		if (d == null) {
			if (DoubleDoor.getSingleton().handleDoor(id, x, y, z)) {
				return true;
			}
			return false;
		}
		int xAdjustment = 0, yAdjustment = 0;
		if (d.type == 0) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = -1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					yAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					yAdjustment = -1;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					yAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					yAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			}
		} else if (d.type == 9) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) { 
			GameObject o = new GameObject(-1, new Position(d.doorX, d.doorY, d.doorZ));
			o.setType(d.type);
			GameServer.getWorld().register(o);
		}
		if (d.doorX == d.originalX && d.doorY == d.originalY) {
			d.doorX += xAdjustment;
			d.doorY += yAdjustment;
		} else { 
			GameObject o = new GameObject(-1, new Position(d.doorX, d.doorY, d.doorZ));
			o.setType(d.type);
			GameServer.getWorld().register(o);
			d.doorX = d.originalX;
			d.doorY = d.originalY;
		}
		if (d.doorId == d.originalId) {
			if (d.open == 0) {
				d.doorId += 1;
			} else if (d.open == 1) {
				d.doorId -= 1;
			}
		} else if (d.doorId != d.originalId) {
			if (d.open == 0) {
				d.doorId -= 1;
			} else if (d.open == 1) {
				d.doorId += 1;
			}
		}
		final GameObject door = new GameObject(d.doorId, new Position(d.doorX, d.doorY, d.doorZ));
		door.setDirection(getNextFace(d));
		door.setType(d.type);
		GameServer.getWorld().register(door);*/
		return true;
	}
	
	@SuppressWarnings("unused")
	private int getNextFace(SingleDoor d) {
		int f = d.originalFace;
		if (d.type == 0) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 2;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 3;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 0;
				} else if (d.originalFace != d.currentFace){
					f = d.originalFace;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 0;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 2;
				} else if (d.originalFace != d.currentFace){
					f = d.originalFace;
				}
			}
		} else if (d.type == 9) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 2;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 0;
				} else if (d.originalFace != d.currentFace){
					f = d.originalFace;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 0;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 2;
				} else if (d.originalFace != d.currentFace){
					f = d.originalFace;
				}
			}
		}
		d.currentFace = f;
		return f;
	}
	
	public void load() {
		/*long start = System.currentTimeMillis();
		System.out.println("Loading single doors...");
		try {
			singleton.processLineByLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded "+ doors.size() +" single doors in "+ (System.currentTimeMillis() - start) +"ms.");*/
	}
	
	@SuppressWarnings("unused")
	private final void processLineByLine() throws FileNotFoundException {
		/*Scanner scanner = new Scanner(new FileReader(doorFile));
	    	try {
	    		while(scanner.hasNextLine()) {
	    			processLine(scanner.nextLine());
	    		}
	  	 } finally {
	    		scanner.close();
	    	}*/
	}
	
	protected void processLine(String line){
		/*Scanner scanner = new Scanner(line);
		scanner.useDelimiter(" ");
		try {
			while(scanner.hasNextLine()) {
				int id = Integer.parseInt(scanner.next());
				int x = Integer.parseInt(scanner.next());
		    		int y = Integer.parseInt(scanner.next());
		    		int f = Integer.parseInt(scanner.next());
		    		int z = Integer.parseInt(scanner.next());
		    		int t = Integer.parseInt(scanner.next());
		    		doors.add(new SingleDoor(id,x,y,z,f,t,alreadyOpen(id)?1:0));
			}
		} finally {
			scanner.close();
		}*/
	}
	
	@SuppressWarnings("unused")
	private boolean alreadyOpen(int id) {
		/*for (int i = 0; i < openDoors.length; i++) {
			if (openDoors[i] == id) {
				return true;
			}
		}*/
		return false;
	}

	@SuppressWarnings("unused")
	private int doorId;
	@SuppressWarnings("unused")
	private int originalId;
	@SuppressWarnings("unused")
	private int doorX;
	@SuppressWarnings("unused")
	private int doorY;
	@SuppressWarnings("unused")
	private int originalX;
	@SuppressWarnings("unused")
	private int originalY;
	@SuppressWarnings("unused")
	private int doorZ;
	private int originalFace;
	private int currentFace;
	private int type;
	private int open;
	
	@SuppressWarnings("unused")
	private static int[] openDoors = {
		1504, 1514, 1517, 1520, 1531, 
		1534, 2033, 2035, 2037, 2998, 
		3271, 4468, 4697, 6101,6103, 
		6105, 6107, 6109, 6111, 6113, 
		6115, 6976, 6978, 8696, 8819,
		10261, 10263,10265,11708,11710,
		11712,11715,11994,12445, 13002,	
	};
	
}