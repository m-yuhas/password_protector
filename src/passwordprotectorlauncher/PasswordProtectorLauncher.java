package passwordprotectorlauncher;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.FileChooser;


public class PasswordProtectorLauncher extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button openButton = new Button("Open");
		Button newButton = new Button("New");
		Button saveButton = new Button("Save");
		Button helpButton = new Button("Help");
		Button aboutButton = new Button("About");
		ScrollPane listPane = new ScrollPane();
		GridPane mainGrid = new GridPane();
		GridPane buttonPane = new GridPane();		
		ColumnConstraints col0 = new ColumnConstraints();
		ColumnConstraints col1 = new ColumnConstraints();
		RowConstraints row0 = new RowConstraints();
		RowConstraints row1 = new RowConstraints();
		FileChooser fileChooser = new FileChooser();
		col0.setPercentWidth(15);
		col1.setPercentWidth(85);
		row0.setPercentHeight(25);
		row1.setPercentHeight(75);
		buttonPane.setHgap(10);
		buttonPane.setVgap(10);
		buttonPane.add(openButton, 0, 0);
		buttonPane.add(newButton, 0, 1);
		buttonPane.add(saveButton, 0, 2);
		buttonPane.add(helpButton, 0, 3);
		buttonPane.add(aboutButton, 0, 4);
		openButton.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						File file = fileChooser.showOpenDialog(primaryStage);
						if (file != null) {
							openFile(file);
						}
					}
				});
		mainGrid.setPadding(new Insets(10, 10, 10, 10));
		mainGrid.setPadding(new Insets(10, 10, 10, 10));
		mainGrid.getColumnConstraints().addAll(col0, col1);
		mainGrid.getRowConstraints().addAll(row0, row1);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.add(buttonPane, 0, 0);
		mainGrid.add(listPane, 1, 0, 1, 2);
		Scene mainScene = new Scene(mainGrid, 800, 800);		
		primaryStage.setTitle("Password Protector");
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	private void openFile(File file) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(file);
		} catch (IOException ex) {
			System.out.println("Error Opening File");
			//TODO: Make this more robust (GUI + Logging)
		}
	}

}
