package dev.clatza.mcautofight;

import dev.clatza.mcautofight.Utils.TimedRemovalList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.Path;

public class GlobalData {
    public static boolean isAttacking = false;
    public static Entity currentTargetEntety = null;
    public static long lastEnemyFoundAt = 0;
    public static TimedRemovalList entityIgnoreList = new TimedRemovalList();
}
