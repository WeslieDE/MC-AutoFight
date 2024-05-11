package dev.clatza.mcautofight.PathFinding;

import dev.clatza.mcautofight.GlobalData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector4i;


//PLANUNG:
//Flächen auf denen ein Spieler steht werden als begehbar markiert, wenn der block unter den füßen kein wasser ist und solid ist.
//Dabei wird die ganze fläche abgescannt und alles was sich nicht in der höhe unterscheidet wird als begehbar markiert
//Das aber nur wenn die fläche größer als 50 zusammenhängende block groß ist.

//Wenn dabei kleine bereiche entstehen die eingekapselt sind, werden diese als nicht begehbar markiert
//Wenn flächen erkannt wird wo wasser ist, wird das wasser 50 block lang abgesannt und als nicht begehbar markiert
//10 block um portal ist unbegehbar

public class MapCreator {
    public static void registerGameEvents(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MinecraftClient.getInstance().player == null) return;

            ChunkCoordinates playerChunk = ChunkCoordinates.getChunkCoordinates(MinecraftClient.getInstance().player.getPos());
            System.out.println(playerChunk);
        });
    }

    private static int getPlayerStandingBlockPos(ClientPlayerEntity player){
        BlockPos pos = player.getBlockPos().down();
        BlockState blockState = player.getWorld().getBlockState(pos);


        return 0;
    }

}
