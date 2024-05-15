package dev.clatza.mcautofight.Controllers;

import dev.clatza.mcautofight.Executors.ActionExecutor;
import dev.clatza.mcautofight.GlobalData;

public class ActionController {
    public static void attack() {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Attack");

        new Thread(() -> {
            try {
                ActionExecutor.doAttack();
            } catch (InterruptedException e) {
                if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Attack: Interrupted.");
            }
        }).start();
    }

//    public static void jump() {
//        if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Jump");
//
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    ActionExecutor.doJump();
//                } catch (InterruptedException e) {
//                    if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Jump: Interrupted.");
//                }
//            }
//        }).start();
//    }

//    public static void sprint() {
//        if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Sprint");
//
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    ActionExecutor.doSprint();
//                } catch (InterruptedException e) {
//                    if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Sprint: Interrupted.");
//                }
//            }
//        }).start();
//    }

//    public static void screenshot() {
//        if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Screenshot");
//
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    ActionExecutor.doScreenshot();
//                } catch (InterruptedException e) {
//                    if (GlobalData.DEBUG) System.out.println("[DEBUG][ActionController] Screenshot: Interrupted.");
//                }
//            }
//        }).start();
//    }
}
