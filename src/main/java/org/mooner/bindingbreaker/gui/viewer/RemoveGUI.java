package org.mooner.bindingbreaker.gui.viewer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mooner.bindingbreaker.BindingBreaker;

import static org.mooner.bindingbreaker.MoonerUtils.chat;
import static org.mooner.bindingbreaker.MoonerUtils.playSound;
import static org.mooner.bindingbreaker.gui.GUIUtils.createItem;

public class RemoveGUI {
    private Inventory inventory;
    private Player p;
    private final Click listener = new Click();
    private final EquipmentSlot slot;

    public RemoveGUI(Player p, EquipmentSlot slot, ItemStack item) {
        this.slot = slot;
        Bukkit.getScheduler().runTaskAsynchronously(BindingBreaker.plugin, () -> {
            this.p = p;
            this.inventory = Bukkit.createInventory(p, 45, chat("정말로 이 아이템을 파괴하시겠습니까?"));
            inventory.setItem(13, item);
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                Bukkit.getScheduler().runTaskLater(BindingBreaker.plugin, () -> {
                    if(inventory != null)
                        inventory.setItem(29, createItem(Material.CLOCK, 5 - finalI, "&a파괴 &7(" + (5 - finalI) + ")", "", "&c주의! 되돌릴 수 없습니다!"));
                }, i * 20);
            }
            Bukkit.getScheduler().runTaskLater(BindingBreaker.plugin, () -> {
                if(inventory != null)
                    inventory.setItem(29, createItem(Material.GREEN_TERRACOTTA, 1, "&a파괴", "", "&c주의! 되돌릴 수 없습니다!"));
            }, 100);
            inventory.setItem(33, createItem(Material.RED_TERRACOTTA, 1, "&c취소"));

            Bukkit.getScheduler().runTask(BindingBreaker.plugin, () -> {
                Bukkit.getPluginManager().registerEvents(listener,BindingBreaker.plugin);
                this.p.openInventory(inventory);
            });
        });
    }

    private class Click implements Listener {
        @EventHandler
        public void onClick(InventoryClickEvent e) {
            if(e.getInventory().equals(inventory)) {
                if(e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
                    return;
                if(e.getClickedInventory().equals(inventory)) {
                    e.setCancelled(true);
                    if(e.getCurrentItem().getType() == Material.CLOCK) return;
                    if(e.getSlot() == 29 && e.getCurrentItem().getType() == Material.GREEN_TERRACOTTA) {
                        switch (slot) {
                            case FEET -> p.getInventory().setBoots(null);
                            case LEGS -> p.getInventory().setLeggings(null);
                            case CHEST -> p.getInventory().setChestplate(null);
                            case HEAD -> p.getInventory().setHelmet(null);
                        }
                        playSound(p, Sound.ENTITY_ITEM_BREAK, 0.8, 1);
                        playSound(p, Sound.ENTITY_ITEM_BREAK, 0.8, 0.75);
                        playSound(p, Sound.ENTITY_ITEM_BREAK, 0.8, 0.5);
                        playSound(p, Sound.BLOCK_ANVIL_DESTROY, 0.4, 0.5);
                        p.sendMessage(chat("&a아이템을 파괴했습니다."));
                    }
                    p.closeInventory();
                }
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(inventory.equals(e.getInventory()) && e.getPlayer().getUniqueId().equals(p.getUniqueId())) {
                HandlerList.unregisterAll(this);
                inventory = null;
                p = null;
            }
        }
    }
}
