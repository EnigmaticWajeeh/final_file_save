



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;


public class final_file_save extends Application{
	static String current_Path = " " ;
	static int file_level=0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
launch(args);
	}
	
	public static void saveimage() throws IOException
	{
		
	System.out.println("In created Mode ");	
	}
	 public static String getFileExtension(String  fileName) {
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
	    }
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		Image text_file = new Image(getClass().getResourceAsStream("text_file.png"));
		Image pdf = new Image(getClass().getResourceAsStream("pdf.gif"));
		Image folder = new Image(getClass().getResourceAsStream("folder.png"));
		ListView<Label> list_view_to_show_file = new ListView<Label>();
		ObservableList<Label> items_to_show_file = FXCollections.observableArrayList();
		Mongo mongo = new MongoClient("localhost",27017);	
		DB db1 = mongo.getDB("fyp1");
	    GridFS photo = new GridFS(db1,"photo"); 
		
		 Group root = new Group();
	        Scene scene = new Scene(root, 551, 400);
	        scene.setOnDragOver(new EventHandler<DragEvent>() {
	            public void handle(DragEvent event) {
	            	//System.out.println("On drag over ");
	                Dragboard db = event.getDragboard();
	                if (db.hasFiles()) {
	                    event.acceptTransferModes(TransferMode.COPY);
	                } else {
	                    event.consume();
	                }
	            }
	        });
	        
	        	        
	     // Dropping over surface
	        scene.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	         //   	System.out.println("Drpped");
	            	 Dragboard db = event.getDragboard();
	                 boolean success = false;
	                 if (db.hasFiles()) {
	                     success = true;
	                     String filePath = null;
	                     
	                     
	                     for (File file:db.getFiles()) {
	                         filePath = file.getAbsolutePath();
	                        
	                         System.out.println(filePath);
	                       //Creates a GridFS instance for the specified bucket in the given database.
	                         File imageFile = new File(filePath);
	                         Path path_of_file = Paths.get(filePath);
	                         BasicFileAttributes attr;
							try {
								attr = Files.readAttributes(path_of_file, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								Date date = new Date();
							
								
								System.out.println(dateFormat.format(date));
								
		                         
		                         if(attr.isRegularFile())
		                         {
		                        	// System.out.println("File extension is: "+getFileExtension(path_of_file.getFileName().toString()));
		                        	 String extension_of_file = getFileExtension(path_of_file.getFileName().toString());
		                        	 Label lbl = new Label();
		                     		lbl.setText(path_of_file.getFileName().toString());
		                     		lbl.setContentDisplay(ContentDisplay.LEFT); //text will be displayed left to icon
		                     		lbl.setGraphicTextGap(10.2);  //Distance between grahpics icon and Text 
		                     		//lbl.setMaxWidth(53.0);
		                     		 
		         
		                        	// System.out.println(extension_of_file);
		                        	 if(extension_of_file.equals("pdf")){
		                        		 lbl.setGraphic(new ImageView(pdf));
		                        	 }
		          	 
		                        	 else if  (extension_of_file.equals("txt")){
		                        		 lbl.setGraphic(new ImageView(text_file));
		                        	 }
		                        	 items_to_show_file.addAll(lbl);
		                        	 list_view_to_show_file.setItems(items_to_show_file);
		                        	
		                        	 
		                        	 DBObject obj = new BasicDBObject();
		                     		obj.put( "path", current_Path+ "/"+path_of_file.getFileName().toString() );
		                     		obj.put("current_Path", current_Path);      
		                     		obj.put("created_date", dateFormat.format(date));
		                     		obj.put("modified_date", dateFormat.format(date));
		                     		obj.put("type", "file");
		                     		obj.put("level", file_level);
		                     		obj.put("level_path", "/"+path_of_file.getFileName().toString());
		                     		 //Creates a GridFS instance for the specified bucket in the given database.
		                     		GridFSInputFile input;
		                     				                     		
		                        	    input = photo.createFile(imageFile);	
		                        	    input.setMetaData(obj);
			                     		//input.setContentType("png");
										input.setFilename(path_of_file.getFileName().toString()); //set filename 
										input.setContentType(extension_of_file);
				                     	input.save();
		                        	 System.out.println("This is the regular file");
		                         }
		                       
		                         
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                       
               	
	                     }
	                                            
	                     
	                 }
	                //event.setDropCompleted(success);
	              //  event.consume();
	            }
	        });
	        
	        
	        
	        
	        
	        
	        //folder options 
	        list_view_to_show_file.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent e) {
					if (e.getButton() == MouseButton.SECONDARY)
		            {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						String folder_name = "new folder";
					
						ContextMenu menu = new ContextMenu();
						MenuItem folder_create = new MenuItem("Create New Folder");
						folder_create.setGraphic(new ImageView(folder));
						folder_create.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								
								 Label lbl = new Label();
								 
		                     		lbl.setText(folder_name);
		                     		lbl.setGraphic(new ImageView(folder));
		                     		lbl.setContentDisplay(ContentDisplay.LEFT); //text will be displayed left to icon
		                     		lbl.setGraphicTextGap(10.2); 
		                     	    lbl.setOnMouseClicked(new EventHandler<MouseEvent>() {

										@Override
										public void handle(MouseEvent mouseEvent) {
											if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
												if(mouseEvent.getClickCount() == 2 ){
													list_view_to_show_file.getItems().clear();
													current_Path = current_Path+ "//" + folder_name;
													file_level++;
													
													
												}
											}
											// TODO Auto-generated method stub
											
										}
									});
		                     		
		                     		
		                     		
		                     		
		                     		
		                     		
		                     		
		                     		
		                     		items_to_show_file.addAll(lbl);
		                        	 list_view_to_show_file.setItems(items_to_show_file);
		                        	 DBObject obj = new BasicDBObject();
			                     		obj.put( "path", current_Path+ "//" + folder_name);
			                     		
			                     		obj.put("current_Path", current_Path);      
			                     		obj.put("created_date", dateFormat.format(date));
			                     		obj.put("modified_date", dateFormat.format(date));
			                     		obj.put("type", "folder");
			                     		obj.put("level", file_level);
			                     		obj.put("level_path", "//"+folder_name);
			                     		 //Creates a GridFS instance for the specified bucket in the given database.
			                     		GridFSInputFile input;
			                     		File image_file = new File("c:\\ali.txt");	
			                     		//if(image_file.isDirectory()){
			                     		//	System.out.println("a directory ");
			                     		//photo.createFile();
			                   	try {
									input = photo.createFile(image_file);
									input.setFilename(folder_name);
									input.setMetaData(obj);
				                   	input.save();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			                   	
			                   	
			                    
			                    
			                     		//input = photo.createFile();
											/*input.setMetaData(obj);
											
											input.setFilename(folder_name); //set filename 
											
											input.save();*/	
			                        	    
								
							}
						});
						MenuItem remove_file = new MenuItem("remove file");
						remove_file.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								
								System.out.println("remove file");
							}
						});
						menu.getItems().addAll(folder_create,remove_file);
						
						list_view_to_show_file.setContextMenu(menu);
						
					/*	Group root = (Group) scene.getRoot();
						root.getChildren().add(textField);
						menu.getItems().addAll(item1);
						
						((Group) root).getChildren().add(menu);*/
						
						
		                System.out.println("Desired Click");
		            }
		            else
		            {
		                System.out.println("No right click");
		            }
				//	items_of_labels.remove(labels.getSelectionModel().getSelectedIndex());
					
					
				}
			});
	        root.getChildren().addAll(list_view_to_show_file);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        
	}

}
