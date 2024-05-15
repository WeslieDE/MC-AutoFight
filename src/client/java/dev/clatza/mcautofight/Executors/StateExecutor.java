package dev.clatza.mcautofight.Executors;

import dev.clatza.mcautofight.Controllers.MovementController;
import dev.clatza.mcautofight.GlobalData;
import dev.clatza.mcautofight.Monitors.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class StateExecutor {
    private static boolean currentState = false;
    private static Thread mainThread = null;

//    public static boolean getState() {
//        return currentState;
//    }

    public static void setAutoFight(boolean newState) {
        if (currentState == newState) {
            if (GlobalData.DEBUG)
                System.out.println("[DEBUG][StateExecutor] setAutoFight: New state == current state. Ignoring.");
            return;
        }

        if (mainThread == null) {
            mainThread = new Thread(() -> {
                if (GlobalData.DEBUG) System.out.println("[DEBUG][StateExecutor] Main thread running.");

                initializing();

                while (true) {
                    try {
                        doMainLogic();
                    } catch (InterruptedException e) {
                        if (GlobalData.DEBUG) System.out.println("[DEBUG][StateExecutor] Main thread interrupted.");
                        stopAllActions();
                        return;
                    }
                }
            });
        }

        if (!newState && mainThread.isAlive()) {
            try {
                mainThread.interrupt();
                mainThread = null;
                currentState = false;
            } catch (Exception e) {
                System.out.println("[ERROR][StateExecutor] mainThread.interrupt(): " + e.getMessage());
            }
        }

        if (newState && !mainThread.isAlive()) {
            try {
                mainThread.start();
                currentState = true;
            } catch (Exception e) {
                System.out.println("[ERROR][StateExecutor] mainThread.start(): " + e.getMessage());
            }
        }

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Auto Klicker is now " + (currentState ? "enabled" : "disabled")));
    }

    // Monitors
    private static LookAtTargetMonitor lookAtTargetMonitor = null;
    private static PortalMonitor portalMonitor = null;
    private static TeleportMonitor teleportMonitor = null;
    private static WalkToTargetMonitor walkToTargetMonitor = null;
    private static AttackTargetMonitor attackTargetMonitor = null;
    private static DisconnectMonitor disconnectMonitor = null;
    private static TargetMonitor targetMonitor = null;

    private static void initializing() {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][StateExecutor] initializing");

        targetMonitor = new TargetMonitor();
        lookAtTargetMonitor = new LookAtTargetMonitor();
        portalMonitor = new PortalMonitor();
        teleportMonitor = new TeleportMonitor();
        walkToTargetMonitor = new WalkToTargetMonitor();
        attackTargetMonitor = new AttackTargetMonitor();
        disconnectMonitor = new DisconnectMonitor();
    }

    private static void doMainLogic() throws InterruptedException {
        targetMonitor.tick();
        teleportMonitor.tick();
        disconnectMonitor.tick();
        portalMonitor.tick();
        walkToTargetMonitor.tick();
        lookAtTargetMonitor.tick();
        attackTargetMonitor.tick();
        Thread.sleep(100);
    }

    private static void stopAllActions() {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][StateExecutor] stopAllActions");

        MovementController.stopAllMovements();
        targetMonitor = null;
        disconnectMonitor = null;
        lookAtTargetMonitor = null;
        portalMonitor = null;
        teleportMonitor = null;
        walkToTargetMonitor = null;
        attackTargetMonitor = null;
    }
}
