package me.apeiros.villagerutil.util;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;

import me.apeiros.villagerutil.Setup;
import me.apeiros.villagerutil.VillagerUtil;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URI;
import java.util.UUID;

@UtilityClass
public class Utils {
    
    // Check if villager trades are locked
    public static boolean villagerTradesLocked(Villager v) {
        return v.getVillagerLevel() != 1 || v.getVillagerExperience() != 0;
    }

    // Remove profession from a villager
    public static void removeProfession(Villager v) {
        v.setProfession(Profession.NONE);
    }

    // Remove profession from a villager and clear its experience
    public static void removeProfessionAndExp(Villager v) {
        removeProfession(v);
        v.setVillagerExperience(0);
        v.setVillagerLevel(1);
    }

    // Create a NamespacedKey
    public static NamespacedKey key(String s) {
        return VillagerUtil.createKey(s);
    }

    // Create a potion
    public static ItemStack makePotion(PotionType type) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        // Null check
        if (meta != null) {
            meta.setBasePotionType(type);
            potion.setItemMeta(meta);
        }

        return potion;
    }

    // Check for token (Automatically true when: Creative mode, Tokens disabled)
    public static boolean hasToken(Player p, Inventory inv) {
        return p.getGameMode() == GameMode.CREATIVE || !VillagerUtil.useTokens() || inv.containsAtLeast(Setup.TOKEN.asOne(), 1);
    }

    // Consume token (Token not consumed when: Creative mode, Tokens disabled)
    public static void removeToken(Player p, Inventory inv) {
        if (p.getGameMode() != GameMode.CREATIVE && VillagerUtil.useTokens()) {
            inv.removeItem(withAmount(Setup.TOKEN.asOne(), 1));
        }
    }

    public static ItemStack withAmount(ItemStack base, int amount) {
        if (base == null) return null;
        ItemStack copy = base.clone();
        copy.setAmount(amount);
        return copy;
    }

    public static ItemStack fromBase64Hash(String hash) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), null);
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URI.create("http://textures.minecraft.net/texture/" + hash).toURL());
        } catch (Exception ignored) {
            // nothing
        }
        profile.setTextures(textures);

        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }
}
