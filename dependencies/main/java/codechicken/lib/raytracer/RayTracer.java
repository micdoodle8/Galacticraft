package codechicken.lib.raytracer;

import codechicken.lib.math.MathHelper;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class RayTracer
{
    private Vector3 vec = new Vector3();
    private Vector3 vec2 = new Vector3();

    private Vector3 s_vec = new Vector3();
    private double s_dist;
    private int s_side;
    private IndexedCuboid6 c_cuboid;
    
    private static ThreadLocal<RayTracer> t_inst = new ThreadLocal<RayTracer>();
    
    public static RayTracer instance()
    {
        RayTracer inst = t_inst.get();
        if(inst == null)
            t_inst.set(inst = new RayTracer());
        return inst;
    }
    
    private void traceSide(int side, Vector3 start, Vector3 end, Cuboid6 cuboid)
    {
        vec.set(start);
        Vector3 hit = null;
        switch(side)
        {
            case 0:
                hit = vec.XZintercept(end, cuboid.min.y);
                break;
            case 1:
                hit = vec.XZintercept(end, cuboid.max.y);
                break;
            case 2:
                hit = vec.XYintercept(end, cuboid.min.z);
                break;
            case 3:
                hit = vec.XYintercept(end, cuboid.max.z);
                break;
            case 4:
                hit = vec.YZintercept(end, cuboid.min.x);
                break;
            case 5:
                hit = vec.YZintercept(end, cuboid.max.x);
                break;
        }
        if(hit == null)
            return;
        
        switch(side)
        {
            case 0:
            case 1:
                if(!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) return;
                break;
            case 2:
            case 3:
                if(!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y)) return;
                break;
            case 4:
            case 5:
                if(!MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) return;
                break;
        }
        
        double dist = vec2.set(hit).subtract(start).magSquared();
        if(dist < s_dist)
        {
            s_side = side;
            s_dist = dist;
            s_vec.set(vec);
        }
    }
    
    public MovingObjectPosition rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid)
    {
        s_dist = Double.MAX_VALUE;
        s_side = -1;
        
        for(int i = 0; i < 6; i++)
            traceSide(i, start, end, cuboid);
        
        if(s_side < 0)
            return null;
        
        MovingObjectPosition mop = new MovingObjectPosition(0, 0, 0, s_side, s_vec.toVec3D());
        mop.typeOfHit = null;
        return mop;
    }

    public MovingObjectPosition rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, BlockCoord pos)
    {
        MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
        if(mop != null)
        {
            mop.typeOfHit = MovingObjectType.BLOCK;
            mop.blockX = pos.x;
            mop.blockY = pos.y;
            mop.blockZ = pos.z;
        }
        return mop;
    }

    public MovingObjectPosition rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, Entity e)
    {
        MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
        if(mop != null)
        {
            mop.typeOfHit = MovingObjectType.ENTITY;
            mop.entityHit = e;
        }
        return mop;
    }
    
    public MovingObjectPosition rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids)
    {
        double c_dist = Double.MAX_VALUE;
        MovingObjectPosition c_hit = null;
        
        for(IndexedCuboid6 cuboid : cuboids)
        {
            MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
            if(mop != null && s_dist < c_dist)
            {
                mop = new ExtendedMOP(mop, cuboid.data, s_dist);
                c_dist = s_dist;
                c_hit = mop;
                c_cuboid = cuboid;
            }
        }
        
        return c_hit;
    }

    public MovingObjectPosition rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockCoord pos, Block block)
    {
        MovingObjectPosition mop = rayTraceCuboids(start, end, cuboids);
        if(mop != null)
        {
            mop.typeOfHit = MovingObjectType.BLOCK;
            mop.blockX = pos.x;
            mop.blockY = pos.y;
            mop.blockZ = pos.z;
            if(block != null)
                c_cuboid.add(new Vector3(-pos.x, -pos.y, -pos.z)).setBlockBounds(block);
        }
        return mop;
    }

    public void rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockCoord pos, Block block, List<ExtendedMOP> hitList)
    {
        for(IndexedCuboid6 cuboid : cuboids)
        {
            MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
            if(mop != null)
            {
                ExtendedMOP emop = new ExtendedMOP(mop, cuboid.data, s_dist);
                emop.typeOfHit = MovingObjectType.BLOCK;
                emop.blockX = pos.x;
                emop.blockY = pos.y;
                emop.blockZ = pos.z;
                hitList.add(emop);
            }
        }
    }

    public static MovingObjectPosition retraceBlock(World world, EntityPlayer player, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);

        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = getBlockReachDistance(player);
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return block.collisionRayTrace(world, x, y, z, headVec, endVec);
    }

    private static double getBlockReachDistance_server(EntityPlayerMP player)
    {
        return player.theItemInWorldManager.getBlockReachDistance();
    }

    @SideOnly(Side.CLIENT)
    private static double getBlockReachDistance_client()
    {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }
    
    public static MovingObjectPosition reTrace(World world, EntityPlayer player)
    {
        return reTrace(world, player, getBlockReachDistance(player));
    }

    public static MovingObjectPosition reTrace(World world, EntityPlayer player, double reach)
    {
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1);
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return world.func_147447_a(headVec, endVec, true, false, true);
    }
    
    public static Vec3 getCorrectedHeadVec(EntityPlayer player)
    {
        Vec3 v = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        if(player.worldObj.isRemote) {
            v.yCoord+=player.getEyeHeight()-player.getDefaultEyeHeight();//compatibility with eye height changing mods
        } else {
            v.yCoord+=player.getEyeHeight();
            if(player instanceof EntityPlayerMP && player.isSneaking())
                v.yCoord-=0.08;
        }
        return v;
    }
    
    public static Vec3 getStartVec(EntityPlayer player)
    {
        return getCorrectedHeadVec(player);
    }
    
    public static double getBlockReachDistance(EntityPlayer player)
    {
        return player.worldObj.isRemote ? getBlockReachDistance_client() : 
            player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
    }
    
    public static Vec3 getEndVec(EntityPlayer player)
    {
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = getBlockReachDistance(player);
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }
}
