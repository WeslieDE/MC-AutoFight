package dev.clatza.mcautofight.Monitors;

import dev.clatza.mcautofight.Controllers.StateController;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PortalMonitor {
    private static final int SEARCH_RADIUS = 250;
    private static final int BUFFER_ZONE = 5;

    private static final List<Box> portalSafeZones = new ArrayList<>();

    public PortalMonitor() {
        portalSafeZones.clear();
        detectPortals();
    }

    public void tick() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        Vec3d position = player.getPos();
        for (Box safeZone : portalSafeZones) {
            if (safeZone.contains(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5)) {
                if (GlobalData.DEBUG) System.out.println("[DEBUG][PortalMonitor] Player is in portal safe zone. AutoFight stopped.");
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("You are in a portal area."));

                //TODO: Change this to return to start position
                StateController.setAutoFight(false);
            }
        }
    }

    public static void detectPortals() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        World world = player.getEntityWorld();
        if(world == null) return;

        BlockPos playerPos = player.getBlockPos();
        BlockPos.Mutable searchPos = new BlockPos.Mutable();
        for (int x = -SEARCH_RADIUS; x <= SEARCH_RADIUS; x++) {
            for (int y = -SEARCH_RADIUS; y <= SEARCH_RADIUS; y++) {
                for (int z = -SEARCH_RADIUS; z <= SEARCH_RADIUS; z++) {
                    searchPos.set(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);
                    if (world.getBlockState(searchPos).getBlock() == Blocks.NETHER_PORTAL) {
                        Box portalBox = new Box(searchPos).expand(BUFFER_ZONE);
                        portalSafeZones.add(portalBox);
                    }
                }
            }
        }
    }


}
