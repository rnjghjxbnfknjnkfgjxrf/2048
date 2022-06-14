package gamepackage.game2048;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Tile extends Label {
    private int value;
    private int x;
    private int y;
    private ImageView imgView;
    private boolean isMerged;
    public Tile() {
        value = 0;
        imgView = new ImageView(new Image("file:0.png"));
        imgView.setFitHeight(75);
        imgView.setFitWidth(75);
        isMerged = false;
        this.setGraphic(imgView);
    }

    public void setValue(int value){
        this.value = value;
        imgView.setImage(new Image(String.format("file:%d.png", value)));
    }

    public int removeValue(){
        int temp = value;
        value = 0;
        return temp;
    }

    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getValue(){return value;}
    public void setMerged(boolean isMerged){this.isMerged = isMerged;}
    public boolean isTileMerged(){return isMerged;}
    public void doubleValue(){
        value *= 2;
        imgView.setImage(new Image(String.format("file:%d.png", value)));
    }
    public int getPos(){return x+y*4;}
}
