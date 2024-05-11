package dev.clatza.mcautofight.PathFinding;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class PathFindingBlockData {
    public ChunkCoordinates chunkCoordinates = null;
    public boolean isWalkable = false;
    public boolean isNoWalkingArea = false;
    public List<Vector3i> neighbors = new ArrayList<>();

    private Vector3i position = null;

    public PathFindingBlockData(int x, int y, int z)  {
        position = new Vector3i(x, y, z);

        Vec3d pos = new Vec3d(x, 0, z);
        chunkCoordinates = ChunkCoordinates.getChunkCoordinates(pos);

        findNeighbors();
    }

    public PathFindingBlockData(Vec3d pos)  {
        position = new Vector3i((int)pos.x, (int)pos.y, (int)pos.z);

        chunkCoordinates = ChunkCoordinates.getChunkCoordinates(pos);

        findNeighbors();
    }

    private void findNeighbors() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        World world = player.getWorld();

        if (!world.getBlockState(new BlockPos(position.x, position.y, position.z)).isAir())
        {
            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x + 1, position.y) == position.y && !world.getBlockState(new BlockPos(position.x + 1, position.y, position.z)).isAir()) neighbors.add(new Vector3i(position.x + 1, position.y, position.z));
            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x - 1, position.y) == position.y && !world.getBlockState(new BlockPos(position.x - 1, position.y, position.z)).isAir()) neighbors.add(new Vector3i(position.x - 1, position.y, position.z));

            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x + 1, position.y + 1) == position.y && !world.getBlockState(new BlockPos(position.x + 1, position.y, position.z + 1)).isAir()) neighbors.add(new Vector3i(position.x + 1, position.y, position.z + 1));
            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x - 1, position.y - 1) == position.y && !world.getBlockState(new BlockPos(position.x - 1, position.y, position.z - 1)).isAir()) neighbors.add(new Vector3i(position.x - 1, position.y, position.z - 1));

            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x + 1, position.y - 1) == position.y && !world.getBlockState(new BlockPos(position.x + 1, position.y, position.z - 1)).isAir()) neighbors.add(new Vector3i(position.x + 1, position.y, position.z - 1));
            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x - 1, position.y + 1) == position.y && !world.getBlockState(new BlockPos(position.x - 1, position.y, position.z + 1)).isAir()) neighbors.add(new Vector3i(position.x - 1, position.y, position.z + 1));

            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x, position.y + 1) == position.y && !world.getBlockState(new BlockPos(position.x, position.y, position.z + 1)).isAir()) neighbors.add(new Vector3i(position.x, position.y, position.z + 1));
            if (world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, position.x, position.y - 1) == position.y && !world.getBlockState(new BlockPos(position.x, position.y, position.z - 1)).isAir()) neighbors.add(new Vector3i(position.x, position.y, position.z - 1));
        }
    }

    @Override
    public String toString() {
        return "PathFindingBlockData{Walkable: " + isWalkable + ", NoWalkingArea: " + isNoWalkingArea + ", ChunkCoordinates: " + chunkCoordinates + "}";
    }

}
