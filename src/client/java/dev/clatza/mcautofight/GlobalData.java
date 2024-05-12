package dev.clatza.mcautofight;

import dev.clatza.mcautofight.Utils.TimedRemovalList;
import net.minecraft.entity.Entity;

public class GlobalData {
    public static boolean DEBUG = true;

    public static boolean isAttacking = false;
    public static Entity currentTargetEntity = null;
    public static long lastEnemyFoundAt = 0;
    public static TimedRemovalList entityIgnoreList = new TimedRemovalList();

    public static int killCounter = 0;
}
