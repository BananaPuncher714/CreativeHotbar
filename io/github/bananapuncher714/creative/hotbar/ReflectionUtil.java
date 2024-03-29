package io.github.bananapuncher714.creative.hotbar;

import org.bukkit.Bukkit;

import io.github.bananapuncher714.creative.hotbar.CreativeHotbar.PacketHandler;

public class ReflectionUtil {
	public static final String VERSION;
	
	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	public static PacketHandler getNewPacketHandlerInstance() {
		try {
			Class< ? > clazz = Class.forName( "io.github.bananapuncher714.creative.hotbar.implementation." + VERSION + ".NMSHandler" );
			return ( PacketHandler ) clazz.newInstance();
		} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e ) {
			e.printStackTrace();
			return null;
		}
	}
}
