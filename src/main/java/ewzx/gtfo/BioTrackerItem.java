package ewzx.gtfo;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.logging.Level;


public class BioTrackerItem extends Item {
    public BioTrackerItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        textConsumer.accept(Text.translatable("tooltip.gtfo.bio_tracker")
                .styled(style -> style.withColor(Formatting.GRAY).withItalic(false)));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(itemStack)) {
            return ActionResult.FAIL;
        }

        user.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        // continuously hold RMB for up to 2 seconds
        return 40;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient() && user instanceof net.minecraft.server.network.ServerPlayerEntity) {
            ServerWorld serverWorld = (ServerWorld) world;

            Box scanBox = user.getBoundingBox().expand(30.0);
            var mobs = world.getEntitiesByClass(MobEntity.class, scanBox, MobEntity::isAlive);

            Vec3d lookVector = user.getRotationVector();

            for (MobEntity mob : mobs) {
                Vec3d mobPos = new Vec3d(mob.getX(), mob.getY(), mob.getZ());
                Vec3d userPos = new Vec3d(user.getX(), user.getY(), user.getZ());

                Vec3d vectorToMob = mobPos.subtract(userPos).normalize();

                double vision = lookVector.dotProduct(vectorToMob);

                if (vision >= 0.0) {
                    Pinging.pingMobs(serverWorld, mob);
                }
            }

            ((ServerPlayerEntity) user).getItemCooldownManager().set(stack, 100);
        }
        return super.finishUsing(stack, world, user);
    }



}
