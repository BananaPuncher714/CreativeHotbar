package io.github.bananapuncher714.creative.hotbar.implementation.v1_14_R1;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.creative.hotbar.CreativeHotbar;
import io.github.bananapuncher714.creative.hotbar.CreativeHotbar.PacketHandler;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_14_R1.PacketPlayInBlockDig.EnumPlayerDigType;

public class NMSHandler implements PacketHandler {

	@Override
	public boolean onPacketIn( Player player, Object packet ) {
		if ( packet instanceof PacketPlayInBlockDig ) {
			// Check for the drop packet
			PacketPlayInBlockDig digPacket = ( PacketPlayInBlockDig ) packet;

			EnumPlayerDigType type = digPacket.d();
			if ( type == EnumPlayerDigType.DROP_ALL_ITEMS || type == EnumPlayerDigType.DROP_ITEM ) {
				// Update the player's hand
				player.getEquipment().setItemInMainHand( player.getEquipment().getItemInMainHand() );
				
				CreativeHotbar.getInstance().toggle( player, type == EnumPlayerDigType.DROP_ITEM );
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isMainThread() {
		return Thread.currentThread() == MinecraftServer.getServer().serverThread;
	}
}