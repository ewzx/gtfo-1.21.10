package ewzx.gtfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.AffineTransformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Pinging {
    public static void initialize() {}

    private record ActivePing(DisplayEntity.BlockDisplayEntity displayEntity, long expireTime) {}

    private static final List<ActivePing> ACTIVE_PINGS = new ArrayList<>();

    public static void pingMobs(ServerWorld world, MobEntity mob) {
        for (net.minecraft.entity.Entity passenger : mob.getPassengerList()) {
            if (passenger instanceof DisplayEntity.BlockDisplayEntity) {
                // If it already has one, we can reset its age/timer here if desired
                return;
            }
        }

        // create block entity
        DisplayEntity.BlockDisplayEntity ping = new DisplayEntity.BlockDisplayEntity(EntityType.BLOCK_DISPLAY, world);
        // set the block visual
        ping.setBlockState(ModBlocks.PING_BLOCK.getDefaultState());

        float scale = 0.3f;
        float halfScale = scale / 2.0f;
        float mobHeight = mob.getHeight();

        // 1. Horizontally: Shift by -halfScale because Block Displays render from their corner.
        // 2. Vertically: Passengers ride near the TOP of a mob. We push it down by half the mob's height,
        //    plus half the block's scale, to lock it perfectly in the absolute geometric center.
        float yOffset = -(mobHeight / 2.0f) - halfScale;

        // adjust scaling
        ping.setTransformation(new AffineTransformation(
                new Vector3f(-halfScale, yOffset, -halfScale),
                null,
                new Vector3f(scale, scale, scale),
                null
        ));

        // spawn at the mobs location
        ping.refreshPositionAndAngles(mob.getX(), mob.getY(), mob.getZ(), 0, 0);
        world.spawnEntity(ping);
        ping.setGlowing(true);
        ping.setGlowColorOverride(0xFF0000);
        // ride the mob
        ping.startRiding(mob, true, false);

        //remove ping
        long expireTime = world.getTime() + 300;
        ACTIVE_PINGS.add(new ActivePing(ping, expireTime));
    }

    public static void tick(ServerWorld world) {
        long currentTime = world.getTime();

        ACTIVE_PINGS.removeIf(ping -> {
            // Check if the ping belongs to this world and if its time is up
            if (ping.displayEntity().getEntityWorld() == world && currentTime >= ping.expireTime()) {
                ping.displayEntity().discard(); // Deletes the entity from the server
                return true; // Removes it from our ACTIVE_PINGS list
            }
            return false;
        });
    }

}
