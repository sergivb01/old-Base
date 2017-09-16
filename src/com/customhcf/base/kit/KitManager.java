
package com.customhcf.base.kit;

import com.customhcf.base.kit.Kit;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface KitManager {
    public static final int UNLIMITED_USES = Integer.MAX_VALUE;

    public List<Kit> getKits();

    public Kit getKit(String var1);

    public Kit getKit(UUID var1);

    public boolean containsKit(Kit var1);

    public void createKit(Kit var1);

    public void removeKit(Kit var1);

    public Inventory getGui(Player var1);

    public void reloadKitData();

    public void saveKitData();
}

