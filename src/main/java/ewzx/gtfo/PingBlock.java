package ewzx.gtfo;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PingBlock extends BlockWithEntity {
    public PingBlock(Settings settings) {
        super(settings);
    }

    public static final MapCodec<PingBlock> CODEC = createCodec(PingBlock::new);

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec(){
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
