
package net.veilmc.base.warp;

import net.veilmc.util.Config;
import net.veilmc.util.GenericUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FlatFileWarpManager
implements WarpManager {
    private String warpDelayWords;
    private long warpDelayMillis;
    private long warpDelayTicks;
    private Map<String, Warp> warpNameMap;
    private final JavaPlugin plugin;
    private Config config;
    private List<Warp> warp = new ArrayList<>();

    public FlatFileWarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.reloadWarpData();
    }

    @Override
    public Collection<String> getWarpNames() {
        return this.warpNameMap.keySet();
    }

    @Override
    public Collection<Warp> getWarps() {
        return this.warpNameMap.values();
    }

    @Override
    public Warp getWarp(String warpName) {
        return this.warpNameMap.get(warpName);
    }

    @Override
    public boolean containsWarp(Warp warp) {
        return this.warp.contains(warp);
    }

    @Override
    public void createWarp(Warp warp) {
        if (this.warp.add(warp)) {
            this.warpNameMap.put(warp.getName(), warp);
        }
    }

    @Override
    public void removeWarp(Warp warp) {
        if (this.warp.remove(warp)) {
            this.warpNameMap.remove(warp.getName());
        }
    }

    @Override
    public String getWarpDelayWords() {
        return this.warpDelayWords;
    }

    @Override
    public long getWarpDelayMillis() {
        return this.warpDelayMillis;
    }

    @Override
    public long getWarpDelayTicks() {
        return this.warpDelayTicks;
    }

    @Override
    public void reloadWarpData() {
        this.config = new Config(this.plugin, "warps");
        Object object = this.config.get("warp");
        if (object instanceof List) {
            this.warp = GenericUtils.createList(object, Warp.class);
            this.warpNameMap = new CaseInsensitiveMap<>();
            for (Warp warp : this.warp) {
                this.warpNameMap.put(warp.getName(), warp);
            }
        }
        this.warpDelayMillis = TimeUnit.SECONDS.toMillis(2);
        this.warpDelayTicks = this.warpDelayMillis / 50;
        this.warpDelayWords = DurationFormatUtils.formatDurationWords(this.warpDelayMillis, true, true);
    }

    @Override
    public void saveWarpData() {
        this.plugin.getConfig().set("warp-delay-millis", this.warpDelayMillis);
        this.plugin.saveConfig();
        this.config.set("warp", this.warp);
        this.config.save();
    }
}

