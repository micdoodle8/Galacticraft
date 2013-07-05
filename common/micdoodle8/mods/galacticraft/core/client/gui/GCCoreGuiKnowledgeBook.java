//package micdoodle8.mods.galacticraft.core.client.gui;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//import org.lwjgl.opengl.GL11;
//import cpw.mods.fml.common.registry.LanguageRegistry;
//
//public class GCCoreGuiKnowledgeBook extends GuiScreen
//{
//    public int guiLeft;
//    public int guiTop;
//    public int xSize = 176;
//    public int ySize = 144;
//    public int currentPage = 0;
//    public final int firstPage = 0;
//    public final int lastPage = 2;
//    GCCoreGuiNextPage nextButton;
//    GCCoreGuiNextPage previousButton;
//
//    @Override
//    public void initGui()
//    {
//        super.initGui();
//        this.buttonList.clear();
//        this.guiLeft = (this.width - this.xSize) / 2;
//        this.guiTop = (this.height - this.ySize) / 2;
//
//        if (this.currentPage != this.firstPage)
//        {
//            this.previousButton = new GCCoreGuiNextPage(1, this.guiLeft + 2, this.guiTop + this.ySize - 23, 1);
//            this.buttonList.add(this.previousButton);
//        }
//
//        if (this.currentPage != this.lastPage)
//        {
//            this.nextButton = new GCCoreGuiNextPage(2, this.guiLeft + this.xSize - 17, this.guiTop + this.ySize - 23, 0);
//            this.buttonList.add(this.nextButton);
//        }
//    }
//
//    @Override
//    protected void actionPerformed(GuiButton buttonClicked)
//    {
//        if (buttonClicked.equals(this.previousButton))
//        {
//            this.currentPage -= 2;
//            this.initGui();
//        }
//        else if (buttonClicked.equals(this.nextButton))
//        {
//            this.currentPage += 2;
//            this.initGui();
//        }
//    }
//
//    @Override
//    public void drawScreen(int par1, int par2, float par3)
//    {
//        GL11.glPushMatrix();
//        this.drawDefaultBackground();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.mc.renderEngine.bindTexture("/mods/galacticraftcore/textures/gui/knowledge.png");
//        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
//        GL11.glPopMatrix();
//
//        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.page.name") + " " + (this.currentPage + 1), this.guiLeft + 30, this.guiTop + this.ySize - 18, 10526880);
//        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.page.name") + " " + (this.currentPage + 2), this.guiLeft + this.xSize - 65, this.guiTop + this.ySize - 18, 10526880);
//
//        super.drawScreen(par1, par2, par3);
//    }
//
//    class GCCoreGuiNextPage extends GuiButton
//    {
//        private final int side;
//
//        public GCCoreGuiNextPage(int par1, int par2, int par3, int side)
//        {
//            super(par1, par2, par3, 15, 15, "");
//            this.side = side;
//        }
//
//        @Override
//        public void drawButton(Minecraft par1Minecraft, int par2, int par3)
//        {
//            if (this.drawButton)
//            {
//                par1Minecraft.renderEngine.bindTexture("/mods/galacticraftcore/textures/gui/knowledge.png");
//                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//                this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
//                this.drawTexturedModalRect(this.xPosition, this.yPosition, 176 + this.side * this.width, 0, this.width, this.height);
//                this.mouseDragged(par1Minecraft, par2, par3);
//            }
//        }
//    }
// }
