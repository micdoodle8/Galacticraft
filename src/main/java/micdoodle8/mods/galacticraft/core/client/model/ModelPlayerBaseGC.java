package micdoodle8.mods.galacticraft.core.client.model;

import api.player.model.ModelPlayer;
import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.smart.render.playerapi.SmartRender;

import java.lang.reflect.Constructor;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ModelPlayerBaseGC extends ModelPlayerBase
{
    public ModelRenderer[] parachute = new ModelRenderer[3];
    public ModelRenderer[] parachuteStrings = new ModelRenderer[4];
    public ModelRenderer[][] tubes = new ModelRenderer[2][7];
    public ModelRenderer[] greenOxygenTanks = new ModelRenderer[2];
    public ModelRenderer[] orangeOxygenTanks = new ModelRenderer[2];
    public ModelRenderer[] redOxygenTanks = new ModelRenderer[2];
    public ModelRenderer oxygenMask;

    private boolean usingParachute;

    protected static IModelCustom frequencyModule;
    public static AbstractClientPlayer playerRendering;
    protected static PlayerGearData currentGearData;

    public static final ResourceLocation oxygenMaskTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/oxygen.png");
    public static final ResourceLocation playerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/player.png");
    public static final ResourceLocation frequencyModuleTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/frequencyModule.png");

    public static boolean isSmartMovingLoaded;
    private static Class modelRotationGCSmartMoving;
    private static Constructor modelRotationGCSmartMovingInit;

    static {
	    isSmartMovingLoaded = Loader.isModLoaded("SmartRender");
	    if (isSmartMovingLoaded)
	    {
		    try {
		    	modelRotationGCSmartMoving = Class.forName("micdoodle8.mods.galacticraft.core.client.model.ModelRotationRendererGC");
		    	modelRotationGCSmartMovingInit = modelRotationGCSmartMoving.getConstructor(ModelBase.class, int.class, int.class, ModelRenderer.class, int.class);
		    } catch (Exception e) { e.printStackTrace(); }
	    }
    }

    /**
     * This is used in place of ModelPlayerGC whenever RenderPlayerAPI is installed
     * It adjusts player limb positions to match Galacticraft movement, arms overhead etc
     * (the arm adjustments take effect even when Smart Moving is installed)
     * 
     * It also renders the Galacticraft equipment, if RenderPlayerAPI but not Smart Moving is installed
     * 
     * @param modelPlayerAPI
     */
    public ModelPlayerBaseGC(ModelPlayerAPI modelPlayerAPI)
    {
        super(modelPlayerAPI);
    }

    private ModelRenderer createModelRenderer(ModelPlayer player, int texOffsetX, int texOffsetY, int type)
    {
    	if (isSmartMovingLoaded)
        {
        	try {
	        	switch (type)
	            {
	        	//Helmet and Frequency Module are head modules
	            case 0:
	            case 9:
	                return (ModelRenderer)modelRotationGCSmartMovingInit.newInstance(player, texOffsetX, texOffsetY, SmartRender.getPlayerBase(this.modelPlayer).getHead(), type);
	            //Oxygen gear etc are body
	            default:
	                return (ModelRenderer)modelRotationGCSmartMovingInit.newInstance(player, texOffsetX, texOffsetY, SmartRender.getPlayerBase(this.modelPlayer).getBody(), type);
	            }
            } catch (Exception e) { e.printStackTrace(); }
        }

    	return new ModelRenderer(player, texOffsetX, texOffsetY);
    }

    private void init()
    {
        float var1 = 0.0F;

        //Do not add GC equipment to the model for armor model - only actual player model
        final Render render = RenderManager.instance.getEntityClassRenderObject(EntityClientPlayerMP.class);
        final ModelBiped modelBipedMain = ((RenderPlayer) render).modelBipedMain;

        if (this.modelPlayer.equals(modelBipedMain))
        {
            this.oxygenMask = createModelRenderer(this.modelPlayer, 0, 0, 0);
            this.oxygenMask.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 1);
            this.oxygenMask.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);

            this.parachute[0] = createModelRenderer(this.modelPlayer, 0, 0, 1).setTextureSize(512, 256);
            this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
            this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
            this.parachute[0].rotateAngleZ = (float) (30F * (Math.PI / 180F));
            this.parachute[1] = createModelRenderer(this.modelPlayer, 0, 42, 1).setTextureSize(512, 256);
            this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, var1);
            this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
            this.parachute[2] = createModelRenderer(this.modelPlayer, 0, 0, 1).setTextureSize(512, 256);
            this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, var1);
            this.parachute[2].setRotationPoint(11F, -11, 0.0F);
            this.parachute[2].rotateAngleZ = (float) -(30F * (Math.PI / 180F));

            this.parachuteStrings[0] = createModelRenderer(this.modelPlayer, 100, 0, 1).setTextureSize(512, 256);
            this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
            this.parachuteStrings[0].rotateAngleZ = (float) (155F * (Math.PI / 180F));
            this.parachuteStrings[0].rotateAngleX = (float) (23F * (Math.PI / 180F));
            this.parachuteStrings[0].setRotationPoint(-9.0F, -7.0F, 2.0F);
            this.parachuteStrings[1] = createModelRenderer(this.modelPlayer, 100, 0, 1).setTextureSize(512, 256);
            this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
            this.parachuteStrings[1].rotateAngleZ = (float) (155F * (Math.PI / 180F));
            this.parachuteStrings[1].rotateAngleX = (float) -(23F * (Math.PI / 180F));
            this.parachuteStrings[1].setRotationPoint(-9.0F, -7.0F, 2.0F);
            this.parachuteStrings[2] = createModelRenderer(this.modelPlayer, 100, 0, 1).setTextureSize(512, 256);
            this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
            this.parachuteStrings[2].rotateAngleZ = (float) -(155F * (Math.PI / 180F));
            this.parachuteStrings[2].rotateAngleX = (float) (23F * (Math.PI / 180F));
            this.parachuteStrings[2].setRotationPoint(9.0F, -7.0F, 2.0F);
            this.parachuteStrings[3] = createModelRenderer(this.modelPlayer, 100, 0, 1).setTextureSize(512, 256);
            this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, var1);
            this.parachuteStrings[3].rotateAngleZ = (float) -(155F * (Math.PI / 180F));
            this.parachuteStrings[3].rotateAngleX = (float) -(23F * (Math.PI / 180F));
            this.parachuteStrings[3].setRotationPoint(9.0F, -7.0F, 2.0F);

            this.tubes[0][0] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][0].setRotationPoint(2F, 3F, 5.8F);
            this.tubes[0][0].setTextureSize(128, 64);
            this.tubes[0][0].mirror = true;
            this.tubes[0][1] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][1].setRotationPoint(2F, 2F, 6.8F);
            this.tubes[0][1].setTextureSize(128, 64);
            this.tubes[0][1].mirror = true;
            this.tubes[0][2] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][2].setRotationPoint(2F, 1F, 6.8F);
            this.tubes[0][2].setTextureSize(128, 64);
            this.tubes[0][2].mirror = true;
            this.tubes[0][3] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][3].setRotationPoint(2F, 0F, 6.8F);
            this.tubes[0][3].setTextureSize(128, 64);
            this.tubes[0][3].mirror = true;
            this.tubes[0][4] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][4].setRotationPoint(2F, -1F, 6.8F);
            this.tubes[0][4].setTextureSize(128, 64);
            this.tubes[0][4].mirror = true;
            this.tubes[0][5] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][5].setRotationPoint(2F, -2F, 5.8F);
            this.tubes[0][5].setTextureSize(128, 64);
            this.tubes[0][5].mirror = true;
            this.tubes[0][6] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[0][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[0][6].setRotationPoint(2F, -3F, 4.8F);
            this.tubes[0][6].setTextureSize(128, 64);
            this.tubes[0][6].mirror = true;

            this.tubes[1][0] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][0].setRotationPoint(-2F, 3F, 5.8F);
            this.tubes[1][0].setTextureSize(128, 64);
            this.tubes[1][0].mirror = true;
            this.tubes[1][1] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][1].setRotationPoint(-2F, 2F, 6.8F);
            this.tubes[1][1].setTextureSize(128, 64);
            this.tubes[1][1].mirror = true;
            this.tubes[1][2] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][2].setRotationPoint(-2F, 1F, 6.8F);
            this.tubes[1][2].setTextureSize(128, 64);
            this.tubes[1][2].mirror = true;
            this.tubes[1][3] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][3].setRotationPoint(-2F, 0F, 6.8F);
            this.tubes[1][3].setTextureSize(128, 64);
            this.tubes[1][3].mirror = true;
            this.tubes[1][4] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][4].setRotationPoint(-2F, -1F, 6.8F);
            this.tubes[1][4].setTextureSize(128, 64);
            this.tubes[1][4].mirror = true;
            this.tubes[1][5] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][5].setRotationPoint(-2F, -2F, 5.8F);
            this.tubes[1][5].setTextureSize(128, 64);
            this.tubes[1][5].mirror = true;
            this.tubes[1][6] = createModelRenderer(this.modelPlayer, 0, 0, 2);
            this.tubes[1][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, var1);
            this.tubes[1][6].setRotationPoint(-2F, -3F, 4.8F);
            this.tubes[1][6].setTextureSize(128, 64);
            this.tubes[1][6].mirror = true;

            this.greenOxygenTanks[0] = createModelRenderer(this.modelPlayer, 4, 0, 3);
            this.greenOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
            this.greenOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
            this.greenOxygenTanks[0].mirror = true;
            this.greenOxygenTanks[1] = createModelRenderer(this.modelPlayer, 4, 0, 4);
            this.greenOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
            this.greenOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
            this.greenOxygenTanks[1].mirror = true;

            this.orangeOxygenTanks[0] = createModelRenderer(this.modelPlayer, 16, 0, 5);
            this.orangeOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
            this.orangeOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
            this.orangeOxygenTanks[0].mirror = true;
            this.orangeOxygenTanks[1] = createModelRenderer(this.modelPlayer, 16, 0, 6);
            this.orangeOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
            this.orangeOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
            this.orangeOxygenTanks[1].mirror = true;

            this.redOxygenTanks[0] = createModelRenderer(this.modelPlayer, 28, 0, 7);
            this.redOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
            this.redOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
            this.redOxygenTanks[0].mirror = true;
            this.redOxygenTanks[1] = createModelRenderer(this.modelPlayer, 28, 0, 8);
            this.redOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, var1);
            this.redOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
            this.redOxygenTanks[1].mirror = true;

            //TODO: Frequency module
            /*
            ModelRenderer fModule = createModelRenderer(this.modelPlayer, 0, 0, 9);
            fModule.addBox(0, 0, 0, 1, 1, 1, var1);
            fModule.setRotationPoint(-2F, 2F, 3.8F);
            fModule.mirror = true;
            */
        }
    }

    @Override
    public void beforeRender(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        if (!(var1 instanceof EntityPlayer)) return;  //Deal with RenderPlayerAPIEnhancer calling this for skeletons etc
        usingParachute = false;

        final EntityPlayer player = (EntityPlayer) var1;
        PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getCommandSenderName());

        if (gearData != null)
        {
            usingParachute = gearData.getParachute() != null;
        }
        playerRendering = (AbstractClientPlayer)var1;
        currentGearData = ClientProxyCore.playerItemData.get(playerRendering.getCommandSenderName());

        if (currentGearData == null)
        {
            String id = player.getGameProfile().getName();

            if (!ClientProxyCore.gearDataRequests.contains(id))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA, new Object[] { id }));
                ClientProxyCore.gearDataRequests.add(id);
            }
        }

        super.beforeRender(var1, var2, var3, var4, var5, var6, var7);

        if (this.oxygenMask == null)
        {
            init();
        }
    }

    @Override
    public void afterSetRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    	super.afterSetRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        if (!(par7Entity instanceof EntityPlayer)) return;  //Deal with RenderPlayerAPIEnhancer calling this for skeletons etc

    	final EntityPlayer player = (EntityPlayer) par7Entity;
    	final ItemStack currentItemStack = player.inventory.getCurrentItem();

    	if (!par7Entity.onGround && par7Entity.worldObj.provider instanceof IGalacticraftWorldProvider && par7Entity.ridingEntity == null && !(currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem))
    	{
    		float speedModifier = 0.1162F * 2;

    		float angularSwingArm = MathHelper.cos(par1 * (speedModifier / 2));
            float rightMod = this.modelPlayer.heldItemRight != 0 ? 1 : 2;
            this.modelPlayer.bipedRightArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * rightMod * par2 * 0.5F;
            this.modelPlayer.bipedLeftArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
            this.modelPlayer.bipedRightArm.rotateAngleX += -angularSwingArm * 4.0F * par2 * 0.5F;
            this.modelPlayer.bipedLeftArm.rotateAngleX += angularSwingArm * 4.0F * par2 * 0.5F;
            this.modelPlayer.bipedLeftLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
            this.modelPlayer.bipedLeftLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2 + (float)Math.PI) * 1.4F * par2;
            this.modelPlayer.bipedRightLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
            this.modelPlayer.bipedRightLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2) * 1.4F * par2;
//    		this.modelPlayer.bipedRightArm.rotateAngleX = -angularSwingArm * 4.0F * par2 * 0.5F;
//    		this.modelPlayer.bipedLeftArm.rotateAngleX = angularSwingArm * 4.0F * par2 * 0.5F;
//    		this.modelPlayer.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.1162F * 2 + (float)Math.PI) * 1.4F * par2;
//    		this.modelPlayer.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.1162F * 2) * 1.4F * par2;
    	}

    	if (usingParachute)
    	{
    		this.modelPlayer.bipedLeftArm.rotateAngleX += (float) Math.PI;
    		this.modelPlayer.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
    		this.modelPlayer.bipedRightArm.rotateAngleX += (float) Math.PI;
    		this.modelPlayer.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
    	}

    	if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof IHoldableItem)
    	{
    		IHoldableItem holdableItem = (IHoldableItem) player.inventory.getCurrentItem().getItem();

    		if (holdableItem.shouldHoldLeftHandUp(player))
    		{
    			this.modelPlayer.bipedLeftArm.rotateAngleX = 0;
    			this.modelPlayer.bipedLeftArm.rotateAngleZ = 0;

    			this.modelPlayer.bipedLeftArm.rotateAngleX += (float) Math.PI + 0.3;
    			this.modelPlayer.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
    		}

    		if (holdableItem.shouldHoldRightHandUp(player))
    		{
    			this.modelPlayer.bipedRightArm.rotateAngleX = 0;
    			this.modelPlayer.bipedRightArm.rotateAngleZ = 0;

    			this.modelPlayer.bipedRightArm.rotateAngleX += (float) Math.PI + 0.3;
    			this.modelPlayer.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
    		}

    		if (player.onGround && holdableItem.shouldCrouch(player))
    		{
    			this.modelPlayer.bipedBody.rotateAngleX = 0.35F;
    			this.modelPlayer.bipedRightLeg.rotationPointZ = 4.0F;
    			this.modelPlayer.bipedLeftLeg.rotationPointZ = 4.0F;
    		}
    	}
    	
        final List<?> l = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));

        for (int i = 0; i < l.size(); i++)
        {
            final Entity e = (Entity) l.get(i);

            if (e instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) e;

                if (ship.riddenByEntity != null && !(ship.riddenByEntity).equals(player) && (ship.getLaunched() || ship.timeUntilLaunch < 390))
                {
                    this.modelPlayer.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(par3 * 0.9F) * 0.2F;
                    this.modelPlayer.bipedRightArm.rotateAngleX = (float) Math.PI;
                    break;
                }
            }
        }
    }
    
    @Override
    public void afterRender(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.afterRender(var1, var2, var3, var4, var5, var6, var7);
        
        //Smart Moving will render through ModelRotationRendererGC instead
        if (ModelPlayerBaseGC.isSmartMovingLoaded)
        	return; 

        //Deal with RenderPlayerAPIEnhancer calling this for skeletons etc
        if (!(var1 instanceof EntityPlayer))
        	return;  

        //Do not render GC equipment on top of armor - only on top of player - see .init() method
        if (this.oxygenMask == null)
        	return;

        final EntityPlayer player = (EntityPlayer) var1;
        PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getCommandSenderName());

        if (var1 instanceof AbstractClientPlayer && gearData != null)
        {
            this.usingParachute = gearData.getParachute() != null;
            boolean wearingMask = gearData.getMask() > -1;
            boolean wearingGear = gearData.getGear() > -1;
            boolean wearingLeftTankGreen = gearData.getLeftTank() == 0;
            boolean wearingLeftTankOrange = gearData.getLeftTank() == 1;
            boolean wearingLeftTankRed = gearData.getLeftTank() == 2;
            boolean wearingRightTankGreen = gearData.getRightTank() == 0;
            boolean wearingRightTankOrange = gearData.getRightTank() == 1;
            boolean wearingRightTankRed = gearData.getRightTank() == 2;
            //boolean wearingFrequencyModule = gearData.getFrequencyModule() > -1;

            if (wearingMask)
            {
            	FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerBaseGC.oxygenMaskTexture);
            	GL11.glPushMatrix();
            	GL11.glScalef(1.05F, 1.05F, 1.05F);
            	this.oxygenMask.rotateAngleY = this.modelPlayer.bipedHead.rotateAngleY;
            	this.oxygenMask.rotateAngleX = this.modelPlayer.bipedHead.rotateAngleX;
            	this.oxygenMask.render(var7);
            	GL11.glScalef(1F, 1F, 1F);
            	GL11.glPopMatrix();
            }

            //TODO: Frequency module
            /*
                if (wearingFrequencyModule)
                {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerBaseGC.frequencyModuleTexture);
                    GL11.glPushMatrix();
                    GL11.glRotatef(180, 1, 0, 0);

                    GL11.glRotatef((float) (this.modelPlayer.bipedHeadwear.rotateAngleY * (-180.0F / Math.PI)), 0, 1, 0);
                    GL11.glRotatef((float) (this.modelPlayer.bipedHeadwear.rotateAngleX * (180.0F / Math.PI)), 1, 0, 0);
                    GL11.glScalef(0.3F, 0.3F, 0.3F);
                    GL11.glTranslatef(-1.1F, 1.2F, 0);
                    this.frequencyModule.renderPart("Main");
                    GL11.glTranslatef(0, 1.2F, 0);
                    GL11.glRotatef((float) (Math.sin(var1.ticksExisted * 0.05) * 50.0F), 1, 0, 0);
                    GL11.glRotatef((float) (Math.cos(var1.ticksExisted * 0.1) * 50.0F), 0, 1, 0);
                    GL11.glTranslatef(0, -1.2F, 0);
                    this.frequencyModule.renderPart("Radar");
                    GL11.glPopMatrix();
                }
             */
            //

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModelPlayerBaseGC.playerTexture);

            if (wearingGear)
            {
            	for (int i = 0; i < 7; i++)
            	{
            		for (int k = 0; k < 2; k++)
            		{
            			this.tubes[k][i].render(var7);
            		}
            	}
            }

            if (wearingLeftTankRed)
            {
            	this.redOxygenTanks[0].render(var7);
            }

            if (wearingLeftTankOrange)
            {
            	this.orangeOxygenTanks[0].render(var7);
            }

            if (wearingLeftTankGreen)
            {
            	this.greenOxygenTanks[0].render(var7);
            }

            if (wearingRightTankRed)
            {
            	this.redOxygenTanks[1].render(var7);
            }

            if (wearingRightTankOrange)
            {
            	this.orangeOxygenTanks[1].render(var7);
            }

            if (wearingRightTankGreen)
            {
            	this.greenOxygenTanks[1].render(var7);
            }

            if (usingParachute)
            {
            	FMLClientHandler.instance().getClient().renderEngine.bindTexture(gearData.getParachute());

            	this.parachute[0].render(var7);
            	this.parachute[1].render(var7);
            	this.parachute[2].render(var7);

            	this.parachuteStrings[0].render(var7);
            	this.parachuteStrings[1].render(var7);
            	this.parachuteStrings[2].render(var7);
            	this.parachuteStrings[3].render(var7);
            }
        }
    }
}
