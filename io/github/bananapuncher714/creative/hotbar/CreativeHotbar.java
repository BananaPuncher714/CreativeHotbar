package io.github.bananapuncher714.creative.hotbar;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.creative.hotbar.tinyprotocol.TinyProtocol;
import io.netty.channel.Channel;

public class CreativeHotbar extends JavaPlugin {
	private static CreativeHotbar INSTANCE;
	
	protected TinyProtocol protocol;
	protected PacketHandler handler;
	
	public void onEnable() {
		INSTANCE = this;
		
		handler = ReflectionUtil.getNewPacketHandlerInstance();
		protocol = new TinyProtocol( this ) {
			@Override
			public Object onPacketInAsync( Player player, Channel channel, Object packet ) {
				return handler.onPacketIn( player, packet ) ? packet : null;
			}
		};
	}
	
	public void toggle( Player player, boolean downShift ) {
		if ( !handler.isMainThread() ) {
			Bukkit.getScheduler().runTask( this, () -> { toggle( player, downShift ); } );
			return;
		}
		// Only work for players in creative mode
		if ( player.getGameMode() != GameMode.CREATIVE ) {
			return;
		}
		// Get the inventory and construct identical container to store results
		PlayerInventory inventory = player.getInventory();
		ItemStack[] contents = inventory.getStorageContents();
		ItemStack[] newContents = new ItemStack[ contents.length ];
		
		int size = contents.length;
		int shift = downShift ? -9 : 9;
		// Shift and set
		for ( int i = 0; i < size; i++ ) {
			newContents[ i ] = contents[ ( size + i + shift ) % size ];
		}
		inventory.setStorageContents( newContents );
	}
	
	public static CreativeHotbar getInstance() {
		return INSTANCE;
	}
	
	public static interface PacketHandler {
		boolean onPacketIn( Player player, Object packet );
		boolean isMainThread();
	}
}
