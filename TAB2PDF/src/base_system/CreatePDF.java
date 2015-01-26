package base_system;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {
	/*
	 * Writes the actual pdf file.
	 */
	public static boolean writePDF(String filename, Tablature tab) throws IOException{
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			if(tab.hasHeader()){
				String[] header = tab.getHeader();
				CreatePDF.drawHeader(cb, header);
			}
			
			if(tab.hasBody()){
			
				String body = tab.getBody();
				//System.out.print(body); // debug
				CreatePDF.drawBody(cb,body);
			}
			
			document.close(); // no need to close PDFWriter?
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static void drawBody(PdfContentByte cb, String body) throws DocumentException, IOException{
		int xLeft = 0;
		int xRight = 1000;
		int yStart = 730;
		int staffSpacing = 70;
		int barLineSpacing = 7;
		int xSpacing = 5;
		int frameIndent = 40;
		String[] lines = body.split("\n");
		
		float xPosition;;
		
		for(int i = 0; i < 6; i++){
			//cb.moveTo(xLeft,yStart - (i*barLineSpacing));
			//cb.lineTo(xRight,yStart - (i*barLineSpacing));
			//cb.stroke();
			
			xPosition = frameIndent;
			for(int j = 0; j < lines[i].length(); j++){
			
				char c = lines[i].charAt(j);
				if(c == '-'){
					cb.moveTo(xPosition,yStart - (i*barLineSpacing));
					xPosition += xSpacing;
					cb.lineTo(xPosition,yStart - (i*barLineSpacing));	
					cb.stroke();
				}
				else if(Character.isDigit(c)){
					cb.beginText();
					BaseFont bf = BaseFont.createFont();
			        cb.setFontAndSize(bf, 9); 
			        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,String.valueOf(c), xPosition +2, yStart - (i*barLineSpacing) -3, 0);
			        cb.endText();
			        xPosition += xSpacing;
			        
				}
				else if(c == '|' && i == 0){ // only draws verticle line when detecting '|' on the first line of staff.
					cb.moveTo(xPosition, yStart);
					cb.lineTo(xPosition, yStart - 5*barLineSpacing);
					cb.stroke();
									
				}

				
				
			}
		}
		/*
		String[] lines = body.split("\n");
		float vSpacing = 0.5f;
		float hSpacing = 5.5f;
		
		for(int i = 0; lines[i].equals("\n"); i++){
			String curLine = lines[i];
			cb.moveTo(xLeft,yStart - (i*staffSpacing));
			cb.lineTo(xRight,yStart - (i*staffSpacing));
			cb.stroke();
		}
		*/
	}
	
	/*
	 * Draws a stylish header.
	 */
	public static boolean drawHeader(PdfContentByte cb, String[] header) throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
        cb.setFontAndSize(bf, 32);   
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, header[0], 275, 800, 0);
        cb.setFontAndSize(bf, 14); 
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, header[1], 275, 780, 0);
        cb.endText();
	return true;	
	}
	
}