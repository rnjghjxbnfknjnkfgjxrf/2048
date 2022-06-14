package gamepackage.game2048;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.Console;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private GridPane grid;
    private List<Tile> tiles;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void hehAction(){
        //Tile tile = (Tile) grid.getChildren().get(new Random().nextInt(16));
        Tile tile = (Tile) grid.getChildren().get(12);
        tile.setValue(2);
    }
    @FXML
    private void hahAction(){
        for (int i = 0; i < 4; i++){
            for (int j = 1; j < 4; j++){
                Tile tile = getTile(i,j);
                if (tile.getValue() == 0) continue;
                Tile leftTile = getLeft(tile);
                if (leftTile == null) continue;
                if (tile.getValue() == leftTile.getValue()){
                    leftTile.doubleValue();
                    tile.setValue(0);
                }
                else if(leftTile.getValue() == 0){
                    leftTile.setValue(tile.getValue());
                    tile.setValue(0);
                }
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tiles = grid.getChildren().stream().map(node -> (Tile)node).collect(Collectors.toList());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                grid.requestFocus();
                Tile tempTile;
                for (int i = 0; i < 16;i++){
                    tempTile = tiles.get(i);
                    tempTile.setX(GridPane.getRowIndex(tempTile));
                    tempTile.setY(GridPane.getColumnIndex(tempTile));
                }
            }
        });
        grid.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case A:
                case D:
                case S:
                case W:
                    makeTurn(event);
                    break;
                case SPACE:
                    hehAction();
                    break;
                case R:
                    resetAction();
                    break;
                default:
                    break;
            }
        });
    }
    @FXML
    private void resetAction(){
        for (Tile tile: tiles){
            tile.setValue(0);
            tile.setMerged(false);
        }
    }
    private Tile getTile(int x, int y){
        return tiles.get(x+y*4);
    }
    private Tile getLeft(Tile tile){
        Tile tempTile;
        Tile leftTile = null;
        for (int i = 1; i < tile.getY()+1; i++){
            tempTile = tiles.get(tile.getPos() - i*4);
            if (tempTile.getValue() == 0) leftTile = tempTile;
            if (tempTile.getValue() == tile.getValue() && !tempTile.isTileMerged()) return tempTile;
        }
        return leftTile;
    }
    private Tile getUpper(Tile tile){
        Tile tempTile;
        Tile upperTile = null;
        for (int i = 1; i < tile.getX() + 1; i++){
            tempTile = tiles.get(tile.getPos() - i);
            if (tempTile.getValue() == 0) upperTile = tempTile;
            if (tempTile.getValue() == tile.getValue() && !tempTile.isTileMerged()) return tempTile;
        }
        return upperTile;
    }
    private Tile getLower(Tile tile){
        Tile tempTile;
        Tile lowerTile = null;
        for (int i = 1; i < 4 - tile.getX(); i++){
            tempTile = tiles.get(tile.getPos() + i);
            if (tempTile.getValue() == 0) lowerTile = tempTile;
            if (tempTile.getValue() == tile.getValue() && !tile.isTileMerged()) return tempTile;
        }
        return lowerTile;
    }
    private Tile getRight(Tile tile){
        Tile tempTile;
        Tile rightTile = null;
        for (int i = 1; i < 4 -tile.getY(); i++){
            tempTile = tiles.get(tile.getPos() + i*4);
            /*if (tempTile.getValue() != 0 && tempTile.getValue() != tile.getValue()){
                System.out.println("im here");
                break;
            }*/
            if (tempTile.getValue() == 0) rightTile = tempTile;
            if (tempTile.getValue() == tile.getValue() && !tempTile.isTileMerged()) return tempTile;
        }
        return rightTile;
    }
    private void merge(Tile first, Tile second){
            first.doubleValue();
            first.setMerged(true);
            second.setValue(0);
    }
    private void move(Tile first, Tile second){
        first.setValue(second.getValue());
        second.setValue(0);
    }
    private void makeTurn(KeyEvent event){
        boolean isAnyMoved = false;
        switch (event.getCode()){
            case A:
                isAnyMoved = leftTurn();
                break;
            case D:
                isAnyMoved = rightTurn();
                break;
            case S:
                isAnyMoved = downTurn();
                break;
            case W:
                isAnyMoved = upTurn();
                break;
            default:
                break;
        }
        for (Tile tile: tiles){
            tile.setMerged(false);
        }
        List<Tile> emptyTiles = tiles.stream().filter(tile -> tile.getValue() == 0).collect(Collectors.toList());
        if (emptyTiles.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Game over");
            alert.setHeaderText("No moves left");
            alert.showAndWait();
        }
        if (isAnyMoved){
            emptyTiles
                    .get(new Random().nextInt(emptyTiles.size()))
                    .setValue(new Random().nextInt(10) > 8? 4:2);
        }
    }
    private boolean leftTurn(){
        Tile tile;
        Tile leftTile;
        int moves = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 1; j < 4; j++){
                tile = getTile(i,j);
                if (tile.getValue() == 0) continue;
                leftTile = getLeft(tile);
                if (leftTile == null) continue;
                if (tile.getValue() == leftTile.getValue()){
                    moves++;
                    merge(leftTile, tile);
                }
                else if(leftTile.getValue() == 0){
                    moves++;
                    move(leftTile, tile);
                }
            }
        }
        return moves > 0;
    }
    private boolean upTurn(){
        Tile tile;
        Tile upperTile;
        int moves = 0;
        for (int i = 1; i < 4; i++){
            for (int j = 0; j < 4; j++){
                tile = getTile(i, j);
                if (tile.getValue() == 0) continue;
                upperTile = getUpper(tile);
                if (upperTile == null) continue;
                if (tile.getValue() == upperTile.getValue()){
                    moves++;
                    merge(upperTile, tile);
                }
                else if(upperTile.getValue() == 0){
                    moves++;
                    move(upperTile, tile);
                }
            }
        }
        return moves > 0;
    }
    private boolean rightTurn(){
        Tile tile;
        Tile rightTile;
        int moves = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 2; j >= 0; j--){
                tile = getTile(i,j);
                if (tile.getValue() == 0) continue;
                rightTile = getRight(tile);
                if (rightTile == null) continue;
                if (tile.getValue() == rightTile.getValue()){
                                /*leftTile.doubleValue();
                                tile.setValue(0);*/
                    moves++;
                    merge(rightTile, tile);
                }
                else if(rightTile.getValue() == 0){
                                /*leftTile.setValue(tile.getValue());
                                tile.setValue(0);*/
                    moves++;
                    move(rightTile, tile);
                }
            }
        }
        return moves > 0;
    }
    private boolean downTurn(){
        Tile tile;
        Tile lowerTile;
        int moves = 0;
        for (int i = 2; i >= 0; i--){
            for (int j = 0; j < 4; j++){
                tile = getTile(i, j);
                if (tile.getValue() == 0) continue;
                lowerTile = getLower(tile);
                if (lowerTile == null) continue;
                if (tile.getValue() == lowerTile.getValue()){
                    moves++;
                    merge(lowerTile, tile);
                }
                else if(lowerTile.getValue() == 0){
                    moves++;
                    move(lowerTile, tile);
                }
            }
        }
        return moves > 0;
    }
}