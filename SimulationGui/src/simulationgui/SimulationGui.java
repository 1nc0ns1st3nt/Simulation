/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationgui;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.*;
import java.io.File;

import SimulationLib.Simulator;
import SimulationLib.Appliance;
import SimulationLib.ApplianceInstance;
import SimulationLib.FuelType;

import javafx.geometry.Pos;
import javafx.event.ActionEvent;

import javafx.application.Platform;

/**
 *
 * @author dyh1g19
 */

public class SimulationGui extends Application {
    private final Simulator simulator;
    private final Pane root;
    private List<ImageView> added_appliances;
    
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    
    private Label priceLabel;
    private Timer timer;
    
    public SimulationGui() {
        super();
        simulator = new Simulator("resources/cost.csv", "resources/costRate.csv");
        root = new Pane();
        added_appliances = new ArrayList();
        priceLabel = new Label();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    private void stopTimer() {
        timer.cancel();
        timer = null;
    }
    
    private String getCostString() {
        return "Gas: " + simulator.totalCostOf(FuelType.Gas) + " Electricity: " + simulator.totalCostOf(FuelType.Electricity);
    }
    private void resumeTimer() {
        timer = new Timer();
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                System.out.println("ran it!");
                simulator.passHour(1);
                Platform.runLater(() -> priceLabel.setText(getCostString()));;
            }
        };

        long delay  = 0L;
        long period = 3000L; // 3 seconds
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }
    
    private TableView getTableView() {
        TableView tableView = new TableView();

        TableColumn<String, Appliance> column1 = new TableColumn<>("Appliance");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column1.setStyle( "-fx-alignment: CENTER;");
        
//        TableColumn<String, Appliance> column2 = new TableColumn<>("Power Rate");
//        column2.setCellValueFactory(new PropertyValueFactory<>("powerRate"));
//        column2.setStyle( "-fx-alignment: CENTER;");

        tableView.getColumns().addAll(column1);
        
        for (Appliance app: this.simulator.appliances) {
//            System.out.println(app + app.getName());
            tableView.getItems().add(app);
        }
        
        tableView.setRowFactory( tv -> {
            TableRow<Appliance> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Appliance rowData = row.getItem();

                    String path = this.getResourceImg("Appliances/" + rowData.name + "_off.png");
                    
                    System.out.println(rowData.name + "->" + path);
                    
                    if (!path.isEmpty()) {
                        String base = path.substring(0, path.length() - 8);
                        
                        ApplianceInstance appInst = simulator.addAppliance(rowData.name);
                        ApplianceView imageView = new ApplianceView(base, path, appEvent -> {
                            if (appEvent.getClickCount() == 2) {
                                System.out.println("Mouse double clicked");
                                
                                ApplianceView view = (ApplianceView)appEvent.getSource();
                                
                                Image newImg = new Image(base + view.getNextState());
                                view.setImage(newImg);
                                
                                view.invertState();
                                appInst.invertState();
                            }
                        });
                        root.getChildren().add(imageView);
                    }
                }
            });
            return row ;
        });
         
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
    }

    private String getResourceImg(String path) {
        File temp = new File("resources/" + path);
        if (temp.exists())
            return temp.toURI().toString();
        return "";
    }
    
    private void addControlOverlay() {
        HBox hbox = new HBox();
           
        Button simulationControlBtn = new Button();
        simulationControlBtn.setText("Start");
        simulationControlBtn.setOnAction(event -> {
            if (timer == null) {
                this.resumeTimer();
                simulationControlBtn.setText("Stop");
            } else {
                this.stopTimer();
                simulationControlBtn.setText("Start");
            }
        });
        
        priceLabel.setText("Gas: 0.0 Electricity: 0.0");
        hbox.getChildren().add(simulationControlBtn);
        hbox.getChildren().add(priceLabel);
        
        hbox.setAlignment(Pos.TOP_RIGHT);
        
        BorderPane pane = new BorderPane();
        pane.setRight(hbox);

        root.getChildren().add(pane);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        HBox hbox = new HBox();

        hbox.getChildren().add(getTableView());
        
        // imageView
        String url = this.getResourceImg("simple_blanked.jpg");
        Image image = new Image(url);
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        
        hbox.getChildren().add(iv1);
        
        root.getChildren().add(hbox);
        addControlOverlay(); // let overlay to over the hbox ??
        
        // scene
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

}
