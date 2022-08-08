package org.mooner.bindingbreaker.gui.viewer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

import static org.mooner.bindingbreaker.gui.GUIUtils.addLore;
import static org.mooner.bindingbreaker.gui.GUIUtils.createItem;

public class BindingGUI {
    private final Player p;
    private Inventory inventory;
    private final Click listener = new Click();

    private static boolean isNull(ItemStack i) {
        return i != null && i.getType() != Material.AIR;
    }

    private static boolean isBind(ItemStack i) {
        return i.getEnchantmentLevel(Enchantment.BINDING_CURSE) > 0;
    }

    public BindingGUI(Player p) {
        this.p = p;
        Bukkit.getScheduler().runTaskAsynchronously(BindingBreaker.plugin, () -> {
            inventory = Bukkit.createInventory(p, 27, "갑옷 파괴하기");
            ItemStack pane = createItem(Material.BLACK_STAINED_GLASS_PANE, 1, " ");
            for (int i = 0; i < 9; i++) inventory.setItem(i, pane);
            for (int i = 9; i < 18; i += 2) inventory.setItem(i, pane);
            for (int i = 18; i < 27; i++) inventory.setItem(i, pane);

            ItemStack barrier = createItem(Material.BARRIER, 1, "&c비어 있음");

            ItemStack helmet = p.getInventory().getHelmet();
            ItemStack cp = p.getInventory().getChestplate();
            ItemStack leg = p.getInventory().getLeggings();
            ItemStack boots = p.getInventory().getBoots();

            inventory.setItem(10, isNull(helmet) ? addLore(helmet.clone(), "", isBind(helmet) ? "&e파괴하려면 클릭하세요." : "&c귀속 저주가 없습니다!") : barrier);
            inventory.setItem(12, isNull(cp) ? addLore(cp.clone(), "", isBind(cp) ? "&e파괴하려면 클릭하세요." : "&c귀속 저주가 없습니다!") : barrier);
            inventory.setItem(14, isNull(leg) ? addLore(leg.clone(), "", isBind(leg) ? "&e파괴하려면 클릭하세요." : "&c귀속 저주가 없습니다!") : barrier);
            inventory.setItem(16, isNull(boots) ? addLore(boots.clone(), "", isBind(boots) ? "&e파괴하려면 클릭하세요." : "&c귀속 저주가 없습니다!") : barrier);

            Bukkit.getScheduler().runTask(BindingBreaker.plugin, () -> {
                Bukkit.getPluginManager().registerEvents(listener, BindingBreaker.plugin);
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
                    if(e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) return;
                    if(isBind(e.getCurrentItem())) {
                        switch (e.getSlot()) {
                            case 10 -> new RemoveGUI(p, EquipmentSlot.HEAD, e.getCurrentItem());
                            case 12 -> new RemoveGUI(p, EquipmentSlot.CHEST, e.getCurrentItem());
                            case 14 -> new RemoveGUI(p, EquipmentSlot.LEGS, e.getCurrentItem());
                            case 16 -> new RemoveGUI(p, EquipmentSlot.FEET, e.getCurrentItem());
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(inventory.equals(e.getInventory()) && e.getPlayer().getUniqueId().equals(p.getUniqueId())) {
                HandlerList.unregisterAll(this);
                inventory = null;
            }
        }
    }
}
