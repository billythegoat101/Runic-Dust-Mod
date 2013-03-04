package dustmod.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelDustBook extends ModelBase
{
    public ModelRenderer rightCover;
    public ModelRenderer leftCover;
    public ModelRenderer rightPageThick;
    public ModelRenderer leftPageThick;
    public ModelRenderer rightFlippyPage;
    public ModelRenderer leftFlippyPage;
    public ModelRenderer spineCover;

    public ModelRenderer stand;
    public ModelRenderer standBack;

//    private float standAngle = 1.56F;
    public ModelDustBook()
    {
        rightCover = (new ModelRenderer(this)).setTextureOffset(0, 0).addBox(-6F, -5F, 0.0F, 6, 10, 0).setTextureSize(140, 74);
        leftCover = (new ModelRenderer(this)).setTextureOffset(16, 0).addBox(0.0F, -5F, 0.0F, 6, 10, 0).setTextureSize(140, 74);
        spineCover = (new ModelRenderer(this)).setTextureOffset(12, 0).addBox(-1F, -5F, 0.0F, 2, 10, 0).setTextureSize(140, 74);
        rightPageThick = (new ModelRenderer(this)).setTextureOffset(0, 10).addBox(0.0F, -4F, -0.99F, 5, 8, 1).setTextureSize(140, 74);
        leftPageThick = (new ModelRenderer(this)).setTextureOffset(12, 10).addBox(0.0F, -4F, -0.01F, 5, 8, 1).setTextureSize(140, 74);
        stand = (new ModelRenderer(this)).setTextureOffset(32, 0).addBox(-6.0F, -4F, -3F, 12, 10, 1).setTextureSize(140, 74);
        standBack = (new ModelRenderer(this)).setTextureOffset(32, 16).addBox(-3.0F, -1F, -7F, 6, 3, 4).setTextureSize(140, 74);
//        standBack = (new ModelRenderer(this)).setTextureOffset(32, 0).addBox(0.0F, -4F, 2F, 5, 8, 1).setTextureSize(140, 74);
        textureWidth = 70;
        textureHeight = 56;
        rightFlippyPage = (new ModelRenderer(this)).setTextureOffset(35, 0).addBox(0.0F, -25.5F, 0.0F, 35, 56, 0).setTextureSize(140, 56);
        leftFlippyPage  = (new ModelRenderer(this)).setTextureOffset(35, 0).addBox(0.0F, -25.5F, 0.0F, 35, 56, 0).setTextureSize(140, 56);
        rightCover.setRotationPoint(0.0F, 0.0F, -1F);
        leftCover.setRotationPoint(0.0F, 0.0F, 1.0F);
        spineCover.rotateAngleY = ((float)Math.PI / 2F);
        rightFlippyPage.rotateAngleX = (float)Math.PI;
        leftFlippyPage.rotateAngleX = (float)Math.PI;
        stand.rotateAngleY = (float)Math.PI / 2;
        standBack.rotateAngleY = (float)Math.PI / 2;
//        standBack.rotateAngleY = -standAngle;
        stand.rotationPointX = MathHelper.sin((float)Math.PI / 2);
        standBack.rotationPointX = MathHelper.sin((float)Math.PI / 2);
//        standBack.rotationPointX = MathHelper.sin(standAngle);
//        rightFlippyPage.rotateAngleZ = (float)Math.PI/2;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        rightCover.render(f5);
        leftCover.render(f5);
        spineCover.render(f5);
        rightPageThick.render(f5);
        leftPageThick.render(f5);
        stand.render(f5);
        standBack.render(f5);
    }

    public void renderPages(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        rightFlippyPage.render(f5);
        leftFlippyPage.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (MathHelper.sin(f * 0.02F) * 0.1F + 1.25F) * f3;
//        System.out.println("f " + f6 + " f3 " + f3);
        rightCover.rotateAngleY = (float)Math.PI + f6;
        leftCover.rotateAngleY = -f6;
        rightPageThick.rotateAngleY = f6;
        leftPageThick.rotateAngleY = -f6;
        rightFlippyPage.rotateAngleY = f6 - f6 * 2.0F * f1;
        leftFlippyPage.rotateAngleY = f6 - f6 * 2.0F * f2;
        rightPageThick.rotationPointX = MathHelper.sin(f6);
        leftPageThick.rotationPointX = MathHelper.sin(f6);
        rightFlippyPage.rotationPointX = MathHelper.sin(f6);
        leftFlippyPage.rotationPointX = MathHelper.sin(f6);
//        rightFlippyPage.rotationPointZ = MathHelper.cos(f6);
//        rightFlippyPage.rotateAngleZ += 0.02;
//        leftFlippyPage.rotateAngleX += 0.02;
//        if(f6 > standAngle) standAngle = f6;
//        stand.rotateAngleY = standAngle;
//        standBack.rotateAngleY = -standAngle;
//        stand.rotationPointX = MathHelper.sin(standAngle);
//        standBack.rotationPointX = MathHelper.sin(standAngle);
    }
}
