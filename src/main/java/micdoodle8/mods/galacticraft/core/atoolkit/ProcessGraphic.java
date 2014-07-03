package micdoodle8.mods.galacticraft.core.atoolkit;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProcessGraphic
{
	public static void go()
	{
		try
		{
			File toProc = new File("toprocess.bmp");
			if (toProc.exists() && toProc.isFile())
			{
				File outFile = new File("toprocessOutput.bmp");
				if (outFile.exists() || !outFile.createNewFile())
				{
					System.out.println("Skipping image processing as output file already exists.");
					return;
				}

				BufferedImage image = BMPDecoder.read(toProc);
				BMPEncoder.write(ProcessGraphic.processedImage(image), outFile);
			}
		}
		catch (IOException e)
		{
			System.out.println("Couldn't open toprocess.bmp image file.");
		}
	}

	private static BufferedImage processedImage(BufferedImage image)
	{
		int height = image.getHeight();
		int width = image.getWidth() / 2;
		BufferedImage outimage = new BufferedImage(width * 2, height, image.getType());

		for (int i = 0; i < height; i++)
		{
			float prop = (i + 0.5F) / (height + 1F) * 2 - 1;
			int phi = (int) (Math.cos(Math.asin(prop)) * width + 0.5);
			if (phi < 1)
			{
				continue;
			}

			Image subimage = image.getSubimage(width - phi, i, phi * 2, 1).getScaledInstance(width * 2, 1, Image.SCALE_SMOOTH);
			BufferedImage outrow = ProcessGraphic.toBufferedImage(subimage);
			for (int j = 0; j < width * 2; j++)
			{
				outimage.setRGB(j, i, outrow.getRGB(j, 0));
			}
		}

		return outimage;
	}

	private static BufferedImage toBufferedImage(Image img)
	{
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}
