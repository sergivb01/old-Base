
package com.customhcf.base.warp;

import com.customhcf.base.warp.Warp;
import java.util.Collection;

public interface WarpManager {
    public Collection<String> getWarpNames();

    public Collection<Warp> getWarps();

    public Warp getWarp(String var1);

    public boolean containsWarp(Warp var1);

    public void createWarp(Warp var1);

    public void removeWarp(Warp var1);

    public String getWarpDelayWords();

    public long getWarpDelayMillis();

    public long getWarpDelayTicks();

    public void reloadWarpData();

    public void saveWarpData();
}

