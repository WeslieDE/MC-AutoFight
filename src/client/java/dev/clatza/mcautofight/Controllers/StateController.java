package dev.clatza.mcautofight.Controllers;

import dev.clatza.mcautofight.Executors.StateExecutor;
import dev.clatza.mcautofight.GlobalData;

public class StateController {
    public static void setAutoFight(boolean autoFight) {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][StateController] Set AutoFight: " + autoFight);
        StateExecutor.setAutoFight(autoFight);
    }
}
