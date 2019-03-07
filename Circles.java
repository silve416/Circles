package circles;

import java.util.stream.Stream;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A lab exercise to introduce Java 8 lambdas and streams.
 * @author Maxwell Silver
 */
public class Circles extends Application {
    
    public int ROWS = 5;
    public int COLS = 5;
    public double CELL_SIZE = 150;
    
    private int row = 0;
    private int col = 0;
    
    public Circles(){
        root = new VBox();
        controls = new HBox();
        canvas = new Pane();    
        
        XLabel = new Label("X Var");
        Xsp = new Spinner<>(-3, 3, 1);
        Xsp.setPrefHeight(1);
        Xsp.setPrefWidth(55);
        
        YLabel = new Label("Y Var");
        Ysp = new Spinner<>(-3, 3, 1);
        Ysp.setPrefHeight(1);
        Ysp.setPrefWidth(55);
        
        
        opaSl = new Slider(0, 1, 1);
        opaSl.setPrefHeight(1);
        opaSl.setPrefWidth(55);
        opaLabel = new Label("Opacity");
        
        colLabel = new Label("Columns");
        colSp = new Spinner<>(1, 5, 5);
        colSp.setPrefHeight(1);
        colSp.setPrefWidth(55);
        
        rowLabel = new Label("Rows");
        rowSp = new Spinner<>(1, 5, 5);
        rowSp.setPrefHeight(1);
        rowSp.setPrefWidth(55);
        
        cellSl = new Slider(0, 1, 1);
        cellSl.setPrefWidth(110);
        cellSl.setMin(50);
        cellSl.setMax(150);
        cellSl.setValue(150);
        cellLabel = new Label("Cell Size: " + (int)cellSl.getValue());
        
        Ysp.valueProperty().addListener(e-> {canvas.getChildren().clear();
        addAllRowsToCanvas(makeAllRows());});
        
        Xsp.valueProperty().addListener(e-> {canvas.getChildren().clear();        
        addAllRowsToCanvas(makeAllRows());});
        
        opaSl.valueProperty().addListener(e-> {canvas.getChildren().clear();
        addAllRowsToCanvas(makeAllRows());});
        
        colSp.valueProperty().addListener(e-> {canvas.getChildren().clear();
        COLS = (int)colSp.getValue();
        addAllRowsToCanvas(makeAllRows());});                
        
        rowSp.valueProperty().addListener(e-> {canvas.getChildren().clear();
        ROWS = (int)rowSp.getValue();
        addAllRowsToCanvas(makeAllRows());});    
        
        cellSl.valueProperty().addListener(e-> {canvas.getChildren().clear();
        CELL_SIZE = cellSl.getValue();
        cellLabel.setText("Cell Size: " +(int)cellSl.getValue());
        addAllRowsToCanvas(makeAllRows());});
        
        rowB = new VBox();
        rowB.setAlignment(Pos.BOTTOM_LEFT);
        rowB.getChildren().addAll(rowLabel, rowSp);
        
        colB = new VBox();
        colB.setAlignment(Pos.BOTTOM_LEFT);
        colB.getChildren().addAll(colLabel, colSp);
        
        cellB = new VBox();
        cellB.setAlignment(Pos.BOTTOM_CENTER);   
        cellB.setPrefWidth(110);
        cellB.getChildren().addAll(cellLabel, cellSl);
        
        XB = new VBox();
        XB.setAlignment(Pos.BOTTOM_RIGHT);
        XB.getChildren().addAll(XLabel, Xsp);
        
        YB = new VBox();
        YB.setAlignment(Pos.BOTTOM_RIGHT);
        YB.getChildren().addAll(YLabel, Ysp);
        
        opaB = new VBox();
        opaB.setAlignment(Pos.BOTTOM_CENTER);
        opaB.getChildren().addAll(opaLabel, opaSl);        
   
        controls.getChildren().addAll(rowB, colB, opaB, cellB, XB, YB);
        controls.setPrefWidth(50);     
        
        root.setAlignment(Pos.CENTER);
        controls.setAlignment(Pos.BOTTOM_CENTER);
        canvas.setPrefSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        
        addAllRowsToCanvas(makeAllRows());
    }
    
    @Override
    public void start(Stage primaryStage) {

        root.getChildren().addAll(canvas, controls);         
        
        primaryStage.setTitle("CRICLESS!!!!11");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        makeAllRows().forEach(r->r.forEach(x->System.out.println(x)));        
    }
    
    private void addToCanvas(Circle obj){
        obj.setFill(new Color(
                Math.random(),
                Math.random(),
                Math.random(), opaSl.getValue()));
        
    double toY = ((this.row * cellSl.getValue()) + (cellSl.getValue() / 2));
    double toX = ((this.col * cellSl.getValue()) + (cellSl.getValue() / 2));
        
    double fromY = ((((int)rowSp.getValue()-1) * cellSl.getValue()) + (cellSl.getValue() / 2));
    double fromX = ((((int)colSp.getValue()-1) * cellSl.getValue()) + (cellSl.getValue() / 2));
        
        obj.setCenterX(fromX);
        obj.setCenterY(fromY);
        
        canvas.getChildren().add(obj);   
        TranslateTransition tt = new TranslateTransition(Duration.millis(500));
        tt.setNode(obj);
        tt.setByX(toX - fromX);
        tt.setByY(toY - fromY);
        tt.play();
        
        ScaleTransition st = new ScaleTransition(Duration.millis(500));
        st.setNode(obj);
        st.setByX((int)Xsp.getValue());
        st.setByY((int)Ysp.getValue());
        st.setByZ(1);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }
    
    private Stream<Circle> makeRow(){
       Stream<Circle> circles = Stream.generate(()->new Circle(0,0,CELL_SIZE/4)).limit(COLS);    
        return circles;
    }
    
    private void addRowToCanvas(Stream<Circle> circles){
        this.col = 0;
        circles.forEach(c-> {addToCanvas(c); col++;});        
    }
    
    private void addAllRowsToCanvas(Stream<Stream<Circle>> circles){
        this.row = 0;
        circles.forEach(r->{addRowToCanvas(r); row++;});
    }
    
    private Stream<Stream<Circle>> makeAllRows(){
       Stream<Stream<Circle>> circles = Stream.generate(()->makeRow()).limit(ROWS);
       return circles;
    }
     
 

    
    private final VBox root;
    private final HBox controls;
    
    private final VBox rowB;
    private final VBox colB;
    private final VBox cellB;
    private final VBox XB;
    private final VBox YB;
    private final VBox opaB;
    
    private final Label rowLabel;
    private final Label colLabel;
    private Label cellLabel;
    private final Label XLabel;
    private final Label YLabel;
    private final Label opaLabel;       
    
    private Pane canvas;
    private Spinner rowSp;
    private Spinner colSp;
    private Slider cellSl;
    private Spinner Xsp;
    private Spinner Ysp;
    private Slider opaSl;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}