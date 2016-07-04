/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.mc.interact.tp.spike;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Max
 */
public class PL extends JavaPlugin implements Listener {

    @EventHandler(ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getItem().getType() != Material.EYE_OF_ENDER || !(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            return;
        }
        getLogger().log(Level.INFO, "Use! {0}", e.getHand());
        e.setCancelled(true);
        e.getPlayer().teleport(e.getPlayer().getWorld().getHighestBlockAt(e.getPlayer().getWorld().getSpawnLocation()).getLocation());
        if (e.getHand() == EquipmentSlot.HAND) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item.getAmount() == 1) {
                e.getPlayer().getInventory().setItemInMainHand(null);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
        } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
            ItemStack item = e.getPlayer().getInventory().getItemInOffHand();
            if (item.getAmount() == 1) {
                e.getPlayer().getInventory().setItemInOffHand(null);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.USE_ITEM) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PL.this.getLogger().log(Level.INFO, "Use received! Hand: {0}", (event.getPacket().getHands().read(0)));
            }

        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.BLOCK_PLACE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PL.this.getLogger().info("Place block");
            }

        });
    }

}
