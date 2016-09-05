package betterwithmods.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class BWConfig 
{
	public static boolean hardcoreGunpowder;
	public static boolean hardcoreLumber;
	public static boolean hardcoreBuckets;
	public static boolean netherSpawn;
	public static boolean slimeSpawn;
	public static boolean debug;
	public static boolean produceDung;
	
	public static void init(File config)
	{
		Configuration cfg = new Configuration(config);
		try
		{
			cfg.load();
			hardcoreGunpowder = cfg.get("Hardcore", "Hardcore Gunpowder", true).getBoolean(true);
			hardcoreLumber = cfg.get("Hardcore", "Hardcore Lumberjack", true).getBoolean(true);
			hardcoreBuckets = cfg.get("Hardcore", "Hardcore Buckets", true).getBoolean(true);
			netherSpawn = cfg.get("Vanilla Tweaks", "Prevent Nether Spawns on Non-Nether Materials", true).getBoolean(true);
			slimeSpawn = cfg.get("Vanilla Tweaks", "Prevent Slimes Spawning on Non-Stone Non-Dirt Materials", true).getBoolean(true);
			debug = cfg.get("Debug", "Debug Mode", false, "Prints Fake Player IDs to console.").getBoolean(false);
			produceDung = cfg.get("Vanilla Tweaks", "Animals Produce Dung", true).getBoolean(true);
		}
		catch(Exception e)
		{}
		finally
		{
			cfg.save();
		}
	}
}