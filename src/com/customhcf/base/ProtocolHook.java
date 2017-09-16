
package com.customhcf.base;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;
import java.lang.invoke.LambdaMetafactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ProtocolHook
{
    private static final ItemStack AIR;

    public static void hook(final BasePlugin basePlugin) {
        final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        final UserManager userManager = basePlugin.getUserManager();
        protocolManager.addPacketListener((PacketListener)new PacketAdapter(basePlugin, new PacketType[] { PacketType.Play.Server.ENTITY_EQUIPMENT }) {
            public void onPacketSending(final PacketEvent event) {
                if (!basePlugin.getServerHandler().useProtocolLib) {
                    return;
                }
                final Player player = event.getPlayer();
                final BaseUser baseUser = userManager.getUser(player.getUniqueId());
                if (!baseUser.isGlintEnabled()) {
                    final PacketContainer packet = event.getPacket();
                    final StructureModifier<ItemStack> modifier = (StructureModifier<ItemStack>)packet.getItemModifier();
                    if (modifier.size() > 0) {
                        final ItemStack stack = (ItemStack)modifier.read(0);
                        if (stack != null && stack.getType() != Material.AIR) {
                            convert(stack);
                        }
                    }
                }
            }
        });
        protocolManager.addPacketListener((PacketListener)new PacketAdapter(basePlugin, new PacketType[] { PacketType.Play.Server.ENTITY_METADATA }) {
            public void onPacketSending(final PacketEvent event) {
                if (!basePlugin.getServerHandler().useProtocolLib) {
                    return;
                }
                final Player player = event.getPlayer();
                final BaseUser baseUser = userManager.getUser(player.getUniqueId());
                if (!baseUser.isGlintEnabled()) {
                    final PacketContainer packet = event.getPacket();
                    final StructureModifier<Entity> modifier = (StructureModifier<Entity>)packet.getEntityModifier(event);
                    if (modifier.size() > 0 && modifier.read(0) instanceof Item) {
                        final WrappedDataWatcher watcher = new WrappedDataWatcher((List)packet.getWatchableCollectionModifier().read(0));
                        if (watcher.size() >= 10) {
                            final ItemStack stack = watcher.getItemStack(10).clone();
                            if (stack != null && stack.getType() != Material.AIR) {
                                convert(stack);
                            }
                        }
                    }
                }
            }
        });
    }

    private static ItemStack convert(final ItemStack origin) {
        if (origin == null || origin.getType() == Material.AIR) {
            return origin;
        }
        switch (origin.getType()) {
            case POTION:
            case GOLDEN_APPLE: {
                if (origin.getDurability() > 0) {
                    origin.setDurability((short)0);
                    break;
                }
                break;
            }
            case ENCHANTED_BOOK: {
                origin.setType(Material.BOOK);
                break;
            }
            default: {
                origin.getEnchantments().keySet().forEach(origin::removeEnchantment);
                break;
            }
        }
        return origin;
    }

    static {
        AIR = new ItemStack(Material.AIR, 1);
    }
}