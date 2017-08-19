package com.cognizant.framework.PDFCompare;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.cognizant.supportlib.GUI;
import com.cognizant.supportlib.Logger;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PDFHighlight {
	
	private String filename1;
	private String filename2;
	private String resultimage;
	private static GUI uiForm = GUI.getInstance();
	private static Logger log = Logger.getInstance();
	
	public PDFHighlight(String filename1, String filename2, String resultimage)
	{
		this.filename1 = filename1;
		this.filename2 = filename2;
		this.resultimage = resultimage;
	}
	
	public PDFHighlight(String filename1, String filename2)
	{
		this.filename1 = filename1;
		this.filename2 = filename2;
		
	}
	
	public String highlightDifferenes()
	{
		try {
			ImageIO.write(
			        getDifferenceImage(
			                ImageIO.read(new File(filename1)),
			                ImageIO.read(new File(filename2))),
			        "png",
			        new File(resultimage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cannot write file for result image");
		}
		return new File(resultimage).getAbsolutePath();
		
		
	}
	
	private BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
	    // convert images to pixel arrays...
		BufferedImage out = null;
		
		final int w = img1.getWidth(),
	            h = img1.getHeight(), 
	            highlight = Color.RED.getRGB();
		final int w2 = img2.getWidth(), h2 = img2.getHeight();
		
		try{
			final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
//			final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);
		    final int[] p2 = img2.getRGB(0, 0, w2, h2, null, 0, w2);
//		    // compare img1 to img2, pixel by pixel. If different, highlight img1's pixel...
		    for (int i = 0; i < (p1.length<=p2.length?p1.length:p2.length); i++) {
		        if (p1[i] != p2[i]) {
		            p1[i] = highlight;
		        }
		    }
//		    // save img1's pixels to a new BufferedImage, and return it...
//		    // (May require TYPE_INT_ARGB)
		    out = new BufferedImage(w<=w2?w:w2, h<=h2?h:h2, BufferedImage.TYPE_INT_RGB);
		    out.setRGB(0, 0, w<=w2?w:w2, h<=h2?h:h2, p1.length<=p2.length?p1:p2, 0, w<=w2?w:w2);	
////		    out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
////		    out.setRGB(0, 0, w, h, p1, 0, w);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	    return out;
		
	}
	
	public double computeImageDifferenceinPercentage()
	{
		BufferedImage img1 = null;
        BufferedImage img2 = null;

        try{            
          
            img1 = ImageIO.read(new File(filename1));
            img2 = ImageIO.read(new File(filename2));
         } catch (IOException e) {
            e.printStackTrace();
         }
         int width1 = img1.getWidth();
         int width2 = img2.getWidth();
         int height1 = img1.getHeight();
         int height2 = img2.getHeight();

         if ((width1 != width2) || (height1 != height2)) {
             System.err.println("Error: Images dimensions mismatch");
             System.exit(1);
         }

         long diff = 0;
         for (int i = 0; i < height1; i++) {
             for (int j = 0; j < width1; j++) {
                 int rgb1 = img1.getRGB(j, i);
                 int rgb2 = img2.getRGB(j, i);
                 int r1 = (rgb1 >> 16) & 0xff;
                 int g1 = (rgb1 >>  8) & 0xff;
                 int b1 = (rgb1      ) & 0xff;
                 int r2 = (rgb2 >> 16) & 0xff;
                 int g2 = (rgb2 >>  8) & 0xff;
                 int b2 = (rgb2      ) & 0xff;
                 diff += Math.abs(r1 - r2);
                 diff += Math.abs(g1 - g2);
                 diff += Math.abs(b1 - b2);
                 
             }
             
         }
         
         
         double n = width1 * height1 * 3;
         //System.out.println(n);
         
         double p = diff / n / 255.0;
        // System.out.println("diff percent: " + (p * 100.0)); 
	
         return p;
	}
	
	public String pageimagematch()
	{
		String status = "FAILED";
		Image image1 = Toolkit.getDefaultToolkit().getImage(filename1);
        Image image2 = Toolkit.getDefaultToolkit().getImage(filename2);

        try 
        {    
            PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
            PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);

            int[] data1 = null;

            if (grab1.getHeight()==grab2.getHeight()&&grab1.getWidth()==grab2.getWidth())
            {
            	//status="PASSED";
            }
            
            
            if (grab1.grabPixels()) 
            {
                int width = grab1.getWidth();
                int height = grab1.getHeight();
                data1 = new int[width * height];
                data1 = (int[]) grab1.getPixels();
            }

            int[] data2 = null;

            if (grab2.grabPixels()) {
                int width = grab2.getWidth();
                int height = grab2.getHeight();
                data2 = new int[width * height];
                data2 = (int[]) grab2.getPixels();
            }

            //System.out.println("Pixels equal: " + java.util.Arrays.equals(data1, data2));
            if (java.util.Arrays.equals(data1, data2))
            {
            	status = "PASSED";
            }

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return status;

	}
	
	private List<String> getText(String filename)
	{
		List<String> listoflinesfrompdf = new ArrayList<>();
		
		try{
			
		listoflinesfrompdf = gettextfrompdf(filename);
		}
		catch (Exception e)
		{
			uiForm.writeLog("Unable to read text frpom file - "+filename);
			log.writeLog("Unable to read text frpom file - "+filename);
		}
		
//		PDDocument document = null;
		
//		try {
//			document = PDDocument.load(new File(filename));
//			uiForm.writeLog("Getting text of pdf using PDFBox");
//			log.writeLog("Getting text of pdf using PDFBox");
//			
//			PDFTextStripper reader = new PDFTextStripper();
//			
//			String text = reader.getText(document).replaceAll("(\\n+)", "~");
//			String[] array = text.split("~");
//			for (int i = 0; i < array.length; i++) {
//				listoflinesfrompdf.add(array[i]);
//			}
			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			listoflinesfrompdf = gettextfrompdf(filename);
//		}
		return listoflinesfrompdf;

	}
	
	private List<String> gettextfrompdf(String filename)
	{
		PdfReader reader = null;
		uiForm.writeLog("Getting text of pdf using iText");
		log.writeLog("Getting text of pdf using iText");
		List<String> listoflinesfrompdf = new ArrayList<String>();

        try {

            reader = new PdfReader(filename);
            

            String textfrompages = "";
            
            
            // pageNumber = 1
            for (int i=1; i<=reader.getNumberOfPages();i++)
            {
            textfrompages = PdfTextExtractor.getTextFromPage(reader, i).replaceAll("(\\n+)", "~");
            String[] array = textfrompages.split("~");
            for (int j = 0; j < array.length; j++) {
				listoflinesfrompdf.add("Page - "+i+" : "+array[j]);
			}
            textfrompages = "";
            
            }
//            String[] array = textfrompages.split("~");
//			for (int i = 0; i < array.length; i++) {
//				listoflinesfrompdf.add(array[i]);
//			}

			
            
            

        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();

        return listoflinesfrompdf;

	}

	public List<String> getmismatchintext()
	{
		List<String> listofmismatchlines = new ArrayList<>();
		//		List<String> linesonlyinfile1 = new ArrayList<>();
		//		List<String> linesonlyinfile2 = new ArrayList<>();


		List<String> linesfromfile1 = new ArrayList<>();
		List<String> linesfromfile2 = new ArrayList<>();

//		List<String> file1mismatch = new ArrayList<>();
//		List<String> file2mismatch = new ArrayList<>();
		try
		{
			linesfromfile1 = getText(filename1);
			linesfromfile2 = getText(filename2);


			if (linesfromfile1.size()>linesfromfile2.size())
			{
				for (int i=0;i<linesfromfile2.size();i++)
				{
					String file1text = "";
					String file2text = "";

					boolean flag = false;
					for (String string : linesfromfile2) {

						if(linesfromfile1.get(i).equals(string))
						{
							flag = true;
							break;
						}
					}

					if (!flag)
					{
						file1text = "File 1: Line "+i+" : "+linesfromfile1.get(i); 
					}

					flag = false;
					for (String string : linesfromfile1) {
						if(linesfromfile2.get(i).equals(string))
						{
							flag = true;
							break;
						}
					}

					if (!flag)
					{
						file2text = "File 2 : Line "+i+" : "+linesfromfile2.get(i);
					}

					if (!(file1text.equals("")&&file2text.equals("")))
					{
						listofmismatchlines.add(file1text+"~"+file2text);
					}



				}
				for (int k = linesfromfile2.size(); k<linesfromfile1.size();k++)
				{
					boolean flag = false;
					String text = "";
					for (String string : linesfromfile2) {
						
						if(linesfromfile1.get(k).equals(string))
						{
							flag = true;
							break;
						}
					}
					
					if (!flag)
					{
						text = "File 1 : Line "+k+" : "+linesfromfile1.get(k);
					}
					
					if (!text.equals(""))
					{
						listofmismatchlines.add(" ~"+text);
					}


  				}
			}
			else if (linesfromfile2.size()>linesfromfile2.size())
			{
				//			for (int i=0;i<linesfromfile1.size();i++)
				//			{
				//				if(!linesfromfile1.get(i).equals(linesfromfile2.get(i)))
				//				{
				//					listofmismatchlines.add("File1: "+linesfromfile1.get(i)+"~"+"File2: "+linesfromfile2.get(i));
				//				}
				//			}
				for (int i=0;i<linesfromfile2.size();i++)
				{
					String file1text = "";
					String file2text = "";

					boolean flag = false;;
					for (String string : linesfromfile1) {

						if(linesfromfile2.get(i).equals(string))
						{
							flag = true;
							break;
						}
					}

					if (!flag)
					{
						file1text = "File 2 : Line "+i+" : "+linesfromfile2.get(i); 
					}

					flag = false;
					for (String string : linesfromfile2) {
						if(linesfromfile1.get(i).equals(string))
						{
							flag = true;
							break;
						}
//						else
//						{
//							flag = false;
//						}
					}

					if (!flag)
					{
						file2text = "File 1 : Line "+i+" : "+linesfromfile1.get(i);
					}

					if (!(file1text.equals("")&&file2text.equals("")))
					{
						listofmismatchlines.add(file1text+"~"+file2text);
					}


				}
				
				for (int k = linesfromfile1.size(); k<linesfromfile2.size();k++)
				{
					boolean flag = false;
					String text = "";
					for (String string : linesfromfile1) {
						
						if(linesfromfile2.get(k).equals(string))
						{
							flag = true;
							break;
						}
					}
					
					if (!flag)
					{
						text = "File 2 : Line "+k+" : "+linesfromfile2.get(k);
					}
					
					if (!text.equals(""))
					{
						listofmismatchlines.add(text+" ~");
					}


  				}
			}
				else
				{
					for (int i=0;i<linesfromfile1.size();i++)
					{
						//				if(!linesfromfile1.get(i).equals(linesfromfile2.get(i)))
						//				{
						//					listofmismatchlines.add("File1: "+linesfromfile1.get(i)+"~"+"File2: "+linesfromfile2.get(i));
						//				}

//						for (int j=0;j<linesfromfile2.size();j++)
//						{
							String file1text = "";
							String file2text = "";

							boolean flag = false;
							for (String string : linesfromfile2) {

								if(linesfromfile1.get(i).equals(string))
								{
									flag = true;
									break;
								}
//								else
//								{
//									flag = false;
//								}
							}
							
							if (!flag)
							{
								file1text = "File 1 : Line "+i+" : "+linesfromfile1.get(i); 
							}

							flag = false;
							for (String string : linesfromfile1) {
								if(linesfromfile2.get(i).equals(string))
								{
									flag = true;
									break;
								}
//								else
//								{
//									flag = false;
//								}
							}

							if (!flag)
							{
								file2text = "File 2 : Line "+i+" : "+linesfromfile2.get(i);
							}

							if (!(file1text.equals("")&&file2text.equals("")))
							{
								listofmismatchlines.add(file1text+"~"+file2text);
							}
//						}


					}

				}
		}
				catch(Exception e)
				{
					System.err.println(e.getMessage());
					e.printStackTrace(System.err);
				}

				return listofmismatchlines;
			}

		}


