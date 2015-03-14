import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.swing.JFileChooser;

import com.itextpdf.text.DocumentException;

public class Controller {

	@FXML
	private TextField inputField;
	@FXML
	private TextField outputField;
	@FXML
	private Button convertButton;
	@FXML
	private ImageView previewPage;
	@FXML
	private TextField spacingSelect;
	@FXML
	private ColorPicker ColorChooser;
	@FXML
	private Button leftPage;
	@FXML
	private Button rightPage;
	@FXML
	private Label pageCounter;
	@FXML
	private Label maxPages;
	@FXML
	private Button BasicMode;
	@FXML
	private ImageView Cover;
	@FXML
	private Label BasicLabel;
	@FXML
	private Label PDFcomplete;
	@FXML
	private Button Back;
	@FXML
	private Button openPDF;
	
	
	private boolean firstTime = true;

	public void setOutputName(ActionEvent event) {
		GUI.outputName = outputField.getText();
	}

	public void showBasicMode(){
		Cover.setVisible(false);
		BasicMode.setVisible(false);
		BasicLabel.setVisible(false);
		
	}
	
	public void browse(ActionEvent event) throws IOException, DocumentException {

		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(fc);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			GUI.inputPath = file.getPath();
			inputField.setText(GUI.inputPath);
			outputField.setText(GUI.inputPath.substring(
					GUI.inputPath.lastIndexOf('\\') + 1,
					GUI.inputPath.length() - 4));
			GUI.outputName = GUI.inputPath.substring(
					GUI.inputPath.lastIndexOf('\\') + 1,
					GUI.inputPath.length() - 4);
			GUI.outputPath = GUI.inputPath.substring(0,
					GUI.inputPath.lastIndexOf('\\') + 1)
					+ GUI.outputName + ".pdf";
		}
		if (GUI.customizeSelected == true && firstTime == true) {
			//Tablature tab = new Tablature(GUI.inputPath, GUI.outputPath);
			DrawPDF.writePDF(new Tablature(GUI.inputPath,GUI.outputPath));
		//	new PDFCreater().writePDF(GUI.outputPath, tab);
			
			previewPage.setImage(PDFPreview.previewPDFDocumentInImage(tab));
			pageCounter.setText(Integer.toString(1));
			maxPages.setText(Integer.toString(PDFPreview.getMaxPage()));
			firstTime = false;
			
			leftPage.setVisible(true);
			leftPage.setDisable(true);
			rightPage.setVisible(true);
			if(PDFPreview.getCurrentPage() == PDFPreview.getMaxPage()){
				rightPage.setDisable(true);
			}
		}
		else if(firstTime == false){
			previewPage.setImage(PDFPreview.updatePreviewPDFDocument());
		}
	}	

	public void convert(ActionEvent event) throws IOException, DocumentException {
		GUI.outputName = outputField.getText();
		GUI.outputPath = GUI.inputPath.substring(0,
				GUI.inputPath.lastIndexOf('\\') + 1)
				+ GUI.outputName + ".pdf";
	
		Tablature tab = new Tablature(GUI.inputPath, GUI.outputPath);
		if (GUI.customizeSelected == true) {
			Creater.setSpacing(Float.parseFloat(spacingSelect.getText()));
		}
	//	new PDFCreater().writePDF(GUI.outputPath, tab);
		PDFcomplete.setVisible(true);
		openPDF.setVisible(true);
	}

	public void showMainMenu(ActionEvent event) throws IOException {
		GUI.customizeSelected = false;
		Parent root = FXMLLoader
				.load(getClass().getResource("/fxml/MainMenu.fxml"));
		Scene scene = new Scene(root, 535, 395);
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
	}

	public void showAdvancedMode(ActionEvent event) throws IOException {
		GUI.customizeSelected = true;
		Parent root = FXMLLoader.load(getClass().getResource(
				"/fxml/AdvancedMode.fxml"));
		Scene scene = new Scene(root, 1046, 768);
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
		
	}
	public void openPDF(){
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File(GUI.outputPath);
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}
	
	
	public void preview() throws IOException, DocumentException {
		
		
		Tablature tab = new Tablature(GUI.inputPath, GUI.outputPath);
		//Creater.setSpacing(Float.parseFloat(spacingSelect.getText()));
		previewPage.setImage(PDFPreview.updatePreviewPDFDocument());
	}

	public void turnRight() throws IOException, DocumentException {

		if (PDFPreview.getCurrentPage() == PDFPreview.getMaxPage()) {
			return;
		}

		leftPage.setDisable(false);

		if (PDFPreview.getCurrentPage() + 1 == PDFPreview.getMaxPage()) {
			rightPage.setDisable(true);
		}

		pageCounter.setText(Integer.toString(Integer.parseInt(pageCounter
				.getText()) + 1));

		if (PDFPreview.getCurrentPage() < PDFPreview.getMaxPage()) {
			PDFPreview.rightPage();
		}
		
		preview();
	}

	public void turnLeft() throws IOException, DocumentException {

		rightPage.setDisable(false);
		if (PDFPreview.getCurrentPage() == 2) {
			leftPage.setDisable(true);
		}

		if (PDFPreview.getCurrentPage() > 1) {
			PDFPreview.leftPage();
		}
		pageCounter.setText(Integer.toString(Integer.parseInt(pageCounter
				.getText()) - 1));
		
		preview();

	}

}
