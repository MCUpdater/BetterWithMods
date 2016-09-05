package betterwithmods.blocks;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import betterwithmods.BWRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockFireStoked extends BlockFire
{
	public BlockFireStoked()
	{
		super();
		this.disableStats();
		this.setLightLevel(1.0F);
		this.setUnlocalizedName("bwm:stokedFire");
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(UPPER, Boolean.valueOf(false)));
	}
	
	@Override
	public int tickRate(World world)
	{
		return 52;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if(!canPlaceBlockAt(world, pos))
			world.setBlockToAir(pos);
		
		BlockPos below = pos.down();
		Block belowBlock = world.getBlockState(below).getBlock();
		
		if(belowBlock == BWRegistry.hibachi)
		{
			if(!belowBlock.isFireSource(world, below, EnumFacing.UP))
				world.setBlockToAir(pos);
		}
		else
		{
			world.setBlockState(pos, Blocks.FIRE.getDefaultState());
			return;
		}
		BlockPos above = pos.up();
		if(world.getBlockState(above).getBlock() == Blocks.BRICK_BLOCK) {
			if(this.isValidKiln(world, above))
				world.setBlockState(above, BWRegistry.kiln.getDefaultState());
		}
		else if(Loader.isModLoaded("terrafirmacraft") && world.getBlockState(above).getBlock() == GameRegistry.findBlock("terrafirmacraft", "FireBrick")) {
			if(this.isValidKiln(world, above))
				world.setBlockState(above, BWRegistry.kiln.getDefaultState());
		}
		
		int meta = world.getBlockState(pos).getValue(AGE);
		
		if(meta < 15)
		{
			meta++;
			world.setBlockState(pos, world.getBlockState(pos).withProperty(AGE, meta));
		}
		
		boolean flag1 = world.isBlockinHighHumidity(pos);/*
		byte b0 = 0;
		if (flag1)
		{
			b0 = -50;
		}*/
		this.tryCatchFire(world, pos.east(), 300, rand, meta, EnumFacing.WEST);
		this.tryCatchFire(world, pos.west(), 300, rand, meta, EnumFacing.EAST);
		this.tryCatchFire(world, pos.down(), 250, rand, meta, EnumFacing.UP);
		this.tryCatchFire(world, pos.up(), 250, rand, meta, EnumFacing.DOWN);
		this.tryCatchFire(world, pos.north(), 300, rand, meta, EnumFacing.SOUTH);
		this.tryCatchFire(world, pos.south(), 300, rand, meta, EnumFacing.NORTH);
		for (int i1 = - 1; i1 <= 1; ++i1)
		{
			for (int j1 = 1; j1 <= 1; ++j1)
			{
				for (int k1 = 1; k1 <= 4; ++k1)
				{
					if (i1 != 0 || k1 != 0 || j1 != 0)
					{
						int l1 = 100;
						if (k1 > 1)
						{
							l1 += (k1 - (1)) * 100;
						}
						BlockPos blockpos = pos.add(i1, k1, j1);
						int i2 = this.getNeighborEncouragement(world, blockpos);
						if (i2 > 0)
						{
							int j2 = (i2 + 40 + world.getDifficulty().getDifficultyId() * 7) / (meta + 30);
							if (flag1)
							{
								j2 /= 2;
							}
							if (j2 > 0 && rand.nextInt(l1) <= j2 && (!world.isRaining() || !this.canDie(world, blockpos)))
							{
								int k2 = meta + rand.nextInt(5) / 4;
								if (k2 > 15)
								{
									k2 = 15;
								}
								world.setBlockState(blockpos, Blocks.FIRE.getDefaultState().withProperty(AGE, k2));
							}
						}
					}
				}
			}
		}
		
		if(meta >= 3)
		{
			world.setBlockState(pos, Blocks.FIRE.getDefaultState().withProperty(AGE, 15), 3);
			return;
		}
		
		world.scheduleBlockUpdate(pos, this, tickRate(world) + world.rand.nextInt(10), 5);
	}

	private boolean isValidKiln(World world, BlockPos pos)
	{
		int numBrick = 0;
		BlockPos center = pos.up();
		for(EnumFacing face : EnumFacing.VALUES)
		{
			if(face == EnumFacing.DOWN) continue;
			Block block = world.getBlockState(center.offset(face)).getBlock();
			if(block == Blocks.BRICK_BLOCK || (Loader.isModLoaded("terrafirmacraft") && block == GameRegistry.findBlock("terrafirmacraft", "FireBrick")))
				numBrick++;
		}
		return numBrick > 2;
	}
	
	private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, int age, EnumFacing face)
    {
        int i = worldIn.getBlockState(pos).getBlock().getFlammability(worldIn, pos, face);

        if (random.nextInt(chance) < i)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos))
            {
                int j = age + random.nextInt(5) / 4;

                if (j > 15)
                {
                    j = 15;
                }

                worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState().withProperty(AGE, Integer.valueOf(j)), 3);
            }
            else
            {
                worldIn.setBlockToAir(pos);
            }

            if (iblockstate.getBlock() == Blocks.TNT)
            {
                Blocks.TNT.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty(BlockTNT.EXPLODE, true));
            }
        }
    }
	
	@Override
	public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AGE, NORTH, EAST, SOUTH, WEST, UPPER);
    }
}