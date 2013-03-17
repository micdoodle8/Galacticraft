package micdoodle8.mods.galacticraft.core.client.render.entities;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockBreathableAir;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreRenderPlayer extends RenderPlayer
{
	public GCCoreRenderPlayer()
	{
		super();
		this.mainModel = new GCCoreModelPlayer(0.0F);
        this.modelBipedMain = (GCCoreModelPlayer) this.mainModel;
        this.modelArmorChestplate = new GCCoreModelPlayer(1.0F);
        this.modelArmor =  new GCCoreModelPlayer(0.5F);
	}
	
	public ModelBiped getModel()
	{
		return this.modelBipedMain;
	}
	
	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        float f1 = 1.0F;
        GL11.glColor3f(f1, f1, f1);
//        super.renderEquippedItems(par1EntityLiving, par2);
        ItemStack itemstack = par1EntityLiving.getHeldItem();
        ItemStack itemstack1 = par1EntityLiving.getCurrentArmor(3);
        float f2;

        if (itemstack1 != null)
        {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack1, BLOCK_3D));

            if (itemstack1.getItem() instanceof ItemBlock)
            {
                if (is3D || RenderBlocks.renderItemIn3d(Block.blocksList[itemstack1.itemID].getRenderType()))
                {
                    f2 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f2, -f2, -f2);
                }

                this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack1, 0);
            }
            else if (itemstack1.getItem().itemID == Item.skull.itemID)
            {
                f2 = 1.0625F;
                GL11.glScalef(f2, -f2, -f2);
                String s = "";

                if (itemstack1.hasTagCompound() && itemstack1.getTagCompound().hasKey("SkullOwner"))
                {
                    s = itemstack1.getTagCompound().getString("SkullOwner");
                }

                TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), s);
            }

            GL11.glPopMatrix();
        }

        if (itemstack != null)
        {
            GL11.glPushMatrix();

            if (this.mainModel.isChild)
            {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.625F, 0.0F);
                GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                GL11.glScalef(f2, f2, f2);
            }

            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D));

            if (itemstack.getItem() instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())))
            {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f2 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f2, -f2, f2);
            }
            else if (itemstack.itemID == Item.bow.itemID)
            {
                f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack.itemID].isFull3D())
            {
                f2 = 0.625F;

                if (Item.itemsList[itemstack.itemID].shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                f2 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f2, f2, f2);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 0);

            if (itemstack.getItem().requiresMultipleRenderPasses())
            {
                for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++)
                {
                    this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, x);
                }
            }

            GL11.glPopMatrix();
        }
    }

    public boolean isAABBInBreathableAirBlock()
    {
        final int var3 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.minX);
        final int var4 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.maxX + 1.0D);
        final int var5 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.minY);
        final int var6 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.maxY + 1.0D);
        final int var7 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.minZ);
        final int var8 = MathHelper.floor_double(FMLClientHandler.instance().getClient().thePlayer.boundingBox.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    final Block var12 = Block.blocksList[FMLClientHandler.instance().getClient().thePlayer.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12 instanceof GCCoreBlockBreathableAir)
                    {
                        final int var13 = FMLClientHandler.instance().getClient().thePlayer.worldObj.getBlockMetadata(var9, var10, var11);
                        double var14 = var10 + 1;

                        if (var13 < 8)
                        {
                            var14 = var10 + 1 - var13 / 8.0D;
                        }

                        if (var14 >= FMLClientHandler.instance().getClient().thePlayer.boundingBox.minY)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        final RenderEngine var3 = RenderManager.instance.renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
            return true;
        }
        else
        {
            return false;
        }
    }

    private ThreadDownloadImageData playerCloak = null;

    @Override
    public void renderSpecialCloak(EntityPlayer var1, float var2)
    {
    	final String string = "http://www.micdoodle8.com/galacticraft/capes/" + StringUtils.stripControlCodes(var1.username) + ".png";

    	if (this.playerCloak == null)
    	{
    		this.playerCloak = RenderManager.instance.renderEngine.obtainImageData(string, new ImageBufferDownload());
    	}

        if (this.loadDownloadableImageTexture(string, (String)null) && !var1.getHasActivePotion() && !var1.getHideCape())
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            final double var3 = var1.field_71091_bM + (var1.field_71094_bP - var1.field_71091_bM) * var2 - (var1.prevPosX + (var1.posX - var1.prevPosX) * var2);
            final double var5 = var1.field_71096_bN + (var1.field_71095_bQ - var1.field_71096_bN) * var2 - (var1.prevPosY + (var1.posY - var1.prevPosY) * var2);
            final double var7 = var1.field_71097_bO + (var1.field_71085_bR - var1.field_71097_bO) * var2 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * var2);
            final float var9 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2;
            final double var10 = MathHelper.sin(var9 * (float)Math.PI / 180.0F);
            final double var12 = -MathHelper.cos(var9 * (float)Math.PI / 180.0F);
            float var14 = (float)var5 * 10.0F;

            if (var14 < -6.0F)
            {
                var14 = -6.0F;
            }

            if (var14 > 32.0F)
            {
                var14 = 32.0F;
            }

            float var15 = (float)(var3 * var10 + var7 * var12) * 100.0F;
            final float var16 = (float)(var3 * var12 - var7 * var10) * 100.0F;

            if (var15 < 0.0F)
            {
                var15 = 0.0F;
            }

            final float var17 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var2;
            var14 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var2) * 6.0F) * 32.0F * var17;

            if (var1.isSneaking())
            {
                var14 += 25.0F;
            }

            GL11.glRotatef(6.0F + var15 / 2.0F + var14, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var16 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var16 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

        	((ModelBiped)this.mainModel).bipedCloak.render(0.0625F);

            GL11.glPopMatrix();
        }
        else
        {
        	super.renderSpecialCloak(var1, var2);
        }
    }
}
