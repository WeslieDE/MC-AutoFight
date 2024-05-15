package dev.clatza.mcautofight.Executors;

import dev.clatza.mcautofight.Utils.TimedRemovalList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class EntitySearchExecutor {
    private static final TimedRemovalList entityIgnoreList = new TimedRemovalList();

    public static AnimalEntity findEntity(PlayerEntity player, boolean random) {
        if (player == null) return null;

        Box searchBox = new Box(player.getBlockPos()).expand(60);
        List<AnimalEntity> animals = player.getWorld().getEntitiesByClass(AnimalEntity.class, searchBox,
                animal -> !entityIgnoreList.contains(animal.getId()));

        if (animals.isEmpty()) return null;

        if (random) {
            Random rand = new Random();
            return animals.get(rand.nextInt(animals.size()));
        } else {
            return animals.stream()
                    .min(Comparator.comparingDouble(a -> calculateWeightedDistance(a, player)))
                    .orElse(null);
        }
    }

    public static void addToIgnoreList(Entity entity) {
        entityIgnoreList.add(entity.getId());
    }

//    public static void clearIgnoreList(Entity entity) {
//        entityIgnoreList.clear();
//    }

    private static double calculateWeightedDistance(net.minecraft.entity.Entity entity, PlayerEntity player) {
        double yWeight = 2.0; // Gewichtung für die Y-Distanz
        double xzWeight = 1.0; // Gewichtung für die X-Z-Distanz
        double deltaY = Math.abs(entity.getY() - player.getY()) * yWeight;
        double deltaXZ = Math.sqrt(Math.pow(entity.getX() - player.getX(), 2) + Math.pow(entity.getZ() - player.getZ(), 2)) * xzWeight;
        return deltaY + deltaXZ;
    }
}
