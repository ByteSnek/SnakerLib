package snaker.snakerlib.level.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;
import snaker.snakerlib.math.SnakerMth;

import java.util.EnumSet;

/**
 * Created by SnakerBone on 21/02/2023
 **/
public class SnakerWanderGoal extends Goal
{
    private final Animal owner;

    public SnakerWanderGoal(Animal owner)
    {
        this.owner = owner;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        RandomSource random = owner.getRandom();

        return owner.getNavigation().isDone() && random.nextInt(3) == 0;
    }

    @Override
    public boolean canContinueToUse()
    {
        return owner.getNavigation().isInProgress();
    }

    @Override
    public void start()
    {
        Vec3i pos = new Vec3i((int) getRandom().x, (int) getRandom().y, (int) getRandom().z);

        owner.getNavigation().moveTo(owner.getNavigation().createPath(new BlockPos(pos), 1), 1);
    }

    private Vec3 getRandom()
    {
        Vec3 view = owner.getViewVector(0);
        Vec3 pos = HoverRandomPos.getPos(owner, 8, 7, view.x, view.z, (SnakerMth.PI / 2), 3, 1);

        return pos != null ? pos : AirAndWaterRandomPos.getPos(owner, 8, 4, -2, view.x, view.z, SnakerMth.PI / 2);
    }
}
