package gamepackage.game2048;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import static gamepackage.game2048.IconsURL.URL;

public class Tile extends Label {
    private int value;
    private int x;
    private int y;
    private final ImageView imgView;
    private boolean isMerged;
    public Tile() {
        value = 0;
        imgView = new ImageView(new Image(URL+"0.png"));
        System.out.println(imgView.getImage());
        imgView.setFitHeight(75);
        imgView.setFitWidth(75);
        isMerged = false;
        this.setGraphic(imgView);
    }

    public void setValue(int value){
        this.value = value;
        imgView.setImage(new Image(URL + String.format("%d.png", value)));
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
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.1), this);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.setToX(0.8);
        scale.setToY(0.8);
        scale.play();
        imgView.setImage(new Image(URL + String.format("%d.png", value)));

    }
    public int getPos(){return x+y*4;}
}
