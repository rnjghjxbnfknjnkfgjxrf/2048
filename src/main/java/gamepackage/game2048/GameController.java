package gamepackage.game2048;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {
    @FXML
    private GridPane grid;
    private List<Tile> tiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tiles = grid.getChildren().stream().map(node -> (Tile)node).collect(Collectors.toList());
        Platform.runLater(() -> {
            grid.requestFocus();
            Tile tempTile;
            for (int i = 0; i < 16;i++){
                tempTile = tiles.get(i);
                tempTile.setX(GridPane.getRowIndex(tempTile));
                tempTile.setY(GridPane.getColumnIndex(tempTile));
            }
            createNewTile(tiles);
        });
        grid.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case A:
                case D:
                case S:
                case W:
                    makeTurn(event);
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
        createNewTile(tiles);
    }
    private void makeTurn(KeyEvent event){
        boolean isAnyMoved = false;
        switch (event.getCode()){
            case LEFT:
            case A:
                isAnyMoved = leftTurn();
                break;
            case RIGHT:
            case D:
                isAnyMoved = rightTurn();
                break;
            case DOWN:
            case S:
                isAnyMoved = downTurn();
                break;
            case UP:
            case W:
                isAnyMoved = upTurn();
                break;
            default:
                break;
        }
        tiles.forEach(tile -> tile.setMerged(false));
        List<Tile> emptyTiles = tiles.stream().filter(tile -> tile.getValue() == 0).collect(Collectors.toList());
        if (emptyTiles.isEmpty()){
            searchForAnyMoves();
        }
        if (isAnyMoved){
            createNewTile(emptyTiles);
        }
    }
    private void createNewTile(List<Tile> emptyTiles){
        Tile newTile = emptyTiles
                .get(new Random().nextInt(emptyTiles.size()));
        newTile.setValue(new Random().nextInt(10) > 8? 4 : 2);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.0), newTile);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
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
                    moves++;
                    merge(rightTile, tile);
                }
                else if(rightTile.getValue() == 0){
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
    private Tile getTile(int x, int y){
        return tiles.get(x+y*4);
    }
    private Tile getLeft(Tile tile){
        Tile tempTile;
        Tile leftTile = null;
        for (int i = 1; i < tile.getY()+1; i++){
            tempTile = tiles.get(tile.getPos() - i*4);
            if (tempTile.getValue() == 0) leftTile = tempTile;
            else if (tempTile.getValue() == tile.getValue() && !tempTile.isTileMerged()) return tempTile;
            else break;
        }
        return leftTile;
    }
    private Tile getUpper(Tile tile){
        Tile tempTile;
        Tile upperTile = null;
        for (int i = 1; i < tile.getX() + 1; i++){
            tempTile = tiles.get(tile.getPos() - i);
            if (tempTile.getValue() == 0) upperTile = tempTile;
            else if (tempTile.getValue() == tile.getValue() && !tempTile.isTileMerged()) return tempTile;
            else break;
        }
        return upperTile;
    }
    private Tile getLower(Tile tile){
        Tile tempTile;
        Tile lowerTile = null;
        for (int i = 1; i < 4 - tile.getX(); i++){
            tempTile = tiles.get(tile.getPos() + i);
            if (tempTile.getValue() == 0) lowerTile = tempTile;
            else if (tempTile.getValue() == tile.getValue() && !tile.isTileMerged()) return tempTile;
            else break;
        }
        return lowerTile;
    }
    private Tile getRight(Tile tile){
        Tile tempTile;
        Tile rightTile = null;
        for (int i = 1; i < 4 -tile.getY(); i++){
            tempTile = tiles.get(tile.getPos() + i*4);
            if (tempTile.getValue() == 0) rightTile = tempTile;
            else if (tempTile.getValue() == tile.getValue() && !tempTile.isTileMerged()) return tempTile;
            else break;
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
    private void gameOverMessage(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Game over");
        alert.setHeaderText("No moves left");
        alert.showAndWait();
    }
    private void searchForAnyMoves(){
        if (tiles
                .stream().noneMatch(tile -> hasSameAbove(tile) ||
                        hasSameAtLeft(tile) ||
                        hasSameAtRight(tile) ||
                        hasSameBelow(tile))) {
            gameOverMessage();
        }
    }
    private boolean hasSameAtRight(Tile tile){
        return tile.getY() < 3 && tile.getValue() == tiles.get(tile.getPos() + 4).getValue();
    }
    private boolean hasSameAtLeft(Tile tile){
        return tile.getY() > 0 && tile.getValue() == tiles.get(tile.getPos() - 4).getValue();
    }
    private boolean hasSameAbove(Tile tile){
        return tile.getX() > 0 && tile.getValue() == tiles.get(tile.getPos() - 1).getValue();
    }
    private boolean hasSameBelow(Tile tile){
        return tile.getX() < 3 && tile.getValue() == tiles.get(tile.getPos() + 1).getValue();
    }
}