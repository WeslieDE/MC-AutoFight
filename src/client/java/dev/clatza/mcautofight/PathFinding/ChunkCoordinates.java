package dev.clatza.mcautofight.PathFinding;

import net.minecraft.util.math.Vec3d;

public class ChunkCoordinates {
    int x;
    int y;
    int a;
    int b;

    public ChunkCoordinates(int x, int y, int a, int b) {
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "ChunkCoordinates{X: " + x + ", Y: " + y + ", A: " + a + ", B: " + b + "}";
    }

    public String getFilePath() {
        return "ChunkCoordinates/" + String.format("%03d", a) + "-" + String.format("%03d", b) + ".png";
    }

    public static ChunkCoordinates getChunkCoordinates(Vec3d pos){
        int newX = (int)pos.x % 1000;
        int newY = (int)pos.y % 1000;
        int a =  (int)pos.x / 1000;
        int b = (int)pos.y / 1000;

        return new ChunkCoordinates(newX, newY, a, b);
    }
}
