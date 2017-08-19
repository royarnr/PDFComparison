package com.cognizant.framework.PDFCompare;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.cognizant.supportlib.GUI;
import com.cognizant.supportlib.Logger;
import com.cognizant.supportlib.TestParameters;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PDFPageExtractor {

	private String filename;
	private static GUI uiForm = GUI.getInstance();
	private static Logger log = Logger.getInstance();

	public PDFPageExtractor(String filename)
	{
		this.filename = filename;
	}



	public List<File> getPDFpageImages(String folderpathtocopy) throws IOException
	{
		List<File> listoffiles = new LinkedList<>();

		BufferedImage imageonpage = null;
		File oldFile = new File(filename);

		if (oldFile.exists()) {
			PDDocument document = null;
			try {
				document = PDDocument.load(oldFile);
				uiForm.writeLog("Load the file using PDFBox");
				log.writeLog("Load the file using PDFBox");
				filename = filename.replace(".pdf", "_listofimages");
				if (!new File(filename).exists())
				{
					new File(filename).mkdirs();
				}
				PDFRenderer render = new PDFRenderer(document);
				for (int i=0; i<document.getNumberOfPages();i++)
				{
					try
					{
					imageonpage = render.renderImage(i);
					//change in page no
					ImageIO.write(imageonpage, "PNG", new File(filename+File.separator+"Page_"+(i+1)+".png"));
					uiForm.writeLog("Extracted image for page "+(i+1));
					log.writeLog("Extracted image for page "+(i+1));
					
					}
					catch (Exception e)
					{
//						break ;
						continue;
					}
				}
				

				FileUtils.copyDirectory(new File(filename), new File(folderpathtocopy));
				

				File[] listofimages = new File(folderpathtocopy).listFiles();
				Arrays.sort(listofimages, new Comparator<File>(){
				    public int compare(File f1, File f2)
				    {
				        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				    } });
				

				for (File file : listofimages) {
					listoffiles.add(file);
				}


				//				Iterator<File> iterator = listoffiles.iterator();
				//				while(iterator.hasNext())
				//				{
				//					System.out.println(iterator.next().getAbsolutePath());
				//				}
				FileUtils.deleteDirectory(new File(filename));


			} catch (InvalidPasswordException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				uiForm.writeLog("Extracting images using the convenstional method");
				log.writeLog("Extracting images using the convenstional method");
				listoffiles = getimageofpages(folderpathtocopy);
			}



		}
		return listoffiles;


	}

	private List<File> getimageofpages(String folderpathtocopy)
	{
		
		List<File> listoffiles = new ArrayList<>();
		File file = new File(filename);
		filename = filename.replace(".pdf", "_listofimages");
		if (!new File(filename).exists())
		{
			new File(filename).mkdirs();
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = null;
		try {
			buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PDFFile pdffile = null;
		try {
			pdffile = new PDFFile(buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numPgs = pdffile.getNumPages();
		for (int i = 1; i <= numPgs; i++) {
			// draw the first page to an image
			PDFPage page = pdffile.getPage(i);
			// get the width and height for the doc at the default zoom
			Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
			// generate the image
			Image img = page.getImage(rect.width, rect.height, // width & height
					rect, // clip rect
					null, // null for the ImageObserver
					true, // fill background with white
					true // block until drawing is done
					);
			// save it as a file
			BufferedImage bImg = toBufferedImage(img);
			try {
				ImageIO.write(bImg, "png", new File(filename+File.separator+"Page_"+(i)+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			FileUtils.copyDirectory(new File(filename), new File(folderpathtocopy));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File[] listofimages = new File(folderpathtocopy).listFiles();
		Arrays.sort(listofimages, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    } });

		for (File imagefile : listofimages) {
			listoffiles.add(imagefile);
		}
		
		try {
			FileUtils.deleteDirectory(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listoffiles;

	}
	
	private BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		boolean hasAlpha = hasAlpha(image);
		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}
			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}

	private boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}
		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}
		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

}

