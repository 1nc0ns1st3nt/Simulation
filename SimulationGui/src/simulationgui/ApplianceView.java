/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationgui;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.image.Image;

/**
 *
 * @author dyh1g19
 */
public class ApplianceView extends ImageView {
    private Boolean isOn;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    
    public ApplianceView(String base, String path, EventHandler<MouseEvent> doubleClickHandler) {
        super(path);
        
        this.isOn = false;
        this.setFitHeight(50);
        this.setFitWidth(50);
        this.setPreserveRatio(true);

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, doubleClickHandler);
        
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->  {
            System.out.println("Mouse pressed");
            orgSceneX = event.getSceneX();
            orgSceneY = event.getSceneY();
            orgTranslateX = this.getTranslateX();
            orgTranslateY = this.getTranslateY();
        });
        
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, event ->  {
            double offsetX = event.getSceneX() - orgSceneX;
            double offsetY = event.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            this.setTranslateX(newTranslateX);
            this.setTranslateY(newTranslateY);
        });
    }
    
    public String getNextState() {
        if (isOn) {
            return "_off.png";
        } else {
            return "_on.png";
        }
    }
    
    public void invertState() {
        this.isOn = !this.isOn;
    }
}
