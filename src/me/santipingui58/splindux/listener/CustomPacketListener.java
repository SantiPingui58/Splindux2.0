package me.santipingui58.splindux.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.hologram.Hologram;
import me.santipingui58.splindux.hologram.HologramManager;


public class CustomPacketListener {

	private List<SpleefPlayer> delay = new ArrayList<SpleefPlayer>();
	
	public CustomPacketListener() {
		packet();
	}
	
	private void packet() {
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(new PacketAdapter(Main.get(),
                ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY) {
			@Override
            public void onPacketReceiving(PacketEvent event) {
				if (event.getPacket()==null) return;
				Player player = event.getPlayer();
            	SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(player);			
                if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {                
                	PacketContainer packet = event.getPacket();
                	for (Hologram h : HologramManager.getManager().getHolograms()) {
                		try {
                		if (packet.getIntegers().read(0).equals(h.getPacketList().get(sp).get(me.santipingui58.splindux.hologram.PacketType.TYPE))) {
                    		if (!delay.contains(sp)) {
                    		HologramManager.getManager().changeChangeType(sp, packet.getIntegers().read(0));
                    		delay.add(sp);
                    		new BukkitRunnable() {
                    		public void run() {
                    			delay.remove(sp);
                    		}	
                    		}.runTaskLater(Main.get(),3L);
                    		} 
                    		return;
                    	} else if (packet.getIntegers().read(0).equals(h.getPacketList().get(sp).get(me.santipingui58.splindux.hologram.PacketType.PERIOD))) {
                    		if (!delay.contains(sp)) {
                    		HologramManager.getManager().changeChangePeriod(sp, packet.getIntegers().read(0));
                    		delay.add(sp);
                    		new BukkitRunnable() {
                    		public void run() {
                    			delay.remove(sp);
                    		}	
                    		}.runTaskLater(Main.get(),3L);
                    		} 
                    		return;
                    	}
                	} catch (Exception e) {}
                	}
                	
                }
			}
            
        });
	}
}
