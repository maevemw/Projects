import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * @author Maeve Wentland
 * @version April 8, 2017
 *
 * This class creates a GUI featuring an interactive Mandelbrot fractal. The
 * GUI allows the user to zoom in and out, move the fractal up, down, right
 * and left and return to the default.
 */
public class MandelbrotGUI extends Application{

    private final int MAX_ITERATIONS = 200;
    private final int SIZE = 600;
    private final int RADIUS = 2;
    private final double zoom = 0.2;
    private final double move = 0.2;
    private WritableImage wImg;
    private ImageView img;


    private final double DEFAULT_LENGTH = 3.6;
    private final double DEFAULT_X_CORNER = -2.3;
    private final double DEFAULT_Y_CORNER = 1.8;

    private double length;
    private double xCorner;
    private double yCorner;

    private BorderPane root;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private Button zoom_in;
    private Button zoom_out;
    private Button original;

    /**
     * Sets the GUI up so it has a row of buttons on the left,
     * and the image of the Mandelbrot as a picture on the right.
     *
     * @param stage is the stage th GUI will be set up in.
     */
    public void start(Stage stage){

        root = new BorderPane();
        VBox vb = new VBox(20);
        vb.setStyle("-fx-background-color: #191970;");
        GridPane gpNav = new GridPane();
        GridPane gpZoom = new GridPane();
        Pane spacer = new Pane();

        //Getting Images
        Image iUp = new Image(getClass().getResourceAsStream("Up.png"));
        Image iDown = new Image(getClass().getResourceAsStream("Down.png"));
        Image iLeft = new Image(getClass().getResourceAsStream("Left.png"));
        Image iRight = new Image(getClass().getResourceAsStream("Right.png"));
        Image in = new Image(getClass().getResourceAsStream("Zoom_In.png"));
        Image out = new Image(getClass().getResourceAsStream("Zoom_Out.png"));

        //Setting up the buttons
        up = new Button();
        down = new Button();
        left = new Button();
        right = new Button();
        zoom_in = new Button();
        zoom_out = new Button();
        original = new Button("DEFAULT");


        //Setting up Graphics
        up.setGraphic(new ImageView(iUp));
        down.setGraphic(new ImageView(iDown));
        left.setGraphic(new ImageView(iLeft));
        right.setGraphic(new ImageView(iRight));
        zoom_in.setGraphic(new ImageView(in));
        zoom_out.setGraphic(new ImageView(out));

        gpNav.add(up,1,1);
        gpNav.add(down,1,3);
        gpNav.add(left,0,2);
        gpNav.add(right,2,2);
        gpZoom.add(zoom_in, 0, 5);
        gpZoom.add(zoom_out, 2, 5);
        vb.getChildren().addAll(gpNav, gpZoom, original);
        vb.setMargin(gpNav,new Insets(50,0,30,0));
        vb.setMargin(gpZoom, new Insets(0,22,0,22));
        vb.setMargin(original,new Insets(30));
        root.setLeft(vb);

        //Setting default values
        length = DEFAULT_LENGTH;
        xCorner = DEFAULT_X_CORNER;
        yCorner = DEFAULT_Y_CORNER;

        //Creating the image
        wImg = new WritableImage(SIZE, SIZE);
        mandelbrot();
        img = new ImageView(wImg);
        root.setCenter(img);
        setHandlers();

        Scene scene = new Scene(root);
        stage.setTitle("Mandelbrot Fractal");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The method creates the Event Handlers for each button on the GUI.
     */
    private void setHandlers(){
        up.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                yCorner = yCorner + move*length;
                mandelbrot();
            }
        });
        down.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                yCorner = yCorner - move*length;
                mandelbrot();
            }
        });
        left.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xCorner = xCorner - move*length;
                mandelbrot();
            }
        });
        right.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xCorner = xCorner + move*length;
                mandelbrot();
            }
        });
        zoom_in.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                yCorner = yCorner - (length * zoom)/2;
                xCorner = xCorner + (length * zoom)/2;
                length = length - length * zoom;
                mandelbrot();
            }
        });
        zoom_out.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                yCorner = yCorner + (length * zoom)/2;
                xCorner = xCorner - (length * zoom)/2;
                length = length + length * zoom;
                mandelbrot();
            }
        });
        original.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                length = DEFAULT_LENGTH;
                yCorner = DEFAULT_Y_CORNER;
                xCorner = DEFAULT_X_CORNER;
                mandelbrot();
            }
        });
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                yCorner = yCorner - event.getY()*(length/SIZE) + length/2;
                xCorner = xCorner + event.getX()*(length/SIZE) - length/2;
                mandelbrot();
            }
        });
        img.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if(event.getDeltaY() > 0) {
                    yCorner = yCorner - (length * zoom) / 2;
                    xCorner = xCorner + (length * zoom) / 2;
                    length = length - length * zoom;
                }
                else{
                    yCorner = yCorner + (length * zoom)/2;
                    xCorner = xCorner - (length * zoom)/2;
                    length = length + length * zoom;
                }
                mandelbrot();
            }
        });
    }

    /**
     * This method runs the math for the Mandelbrot set where C is a complex number
     * and it performs the following algorithm:
     * Z_0 = C
     * Z_1 = Z_0^2 + C
     * ...
     * Z_n = Z_n-1^2 + C
     * until the modulus of Z_n is greater than two or is reaches the end of the MAX_ITERATIONS.
     *
     * @param C if the complex number corresponding to the point in the complex plane being calculated
     * @return the number of iterations before the modulus of Z_n went out of the radius.
     */
    private int doIterations(Complex C){
        Complex Z = new Complex(C);
        int counter = 0;
        while(Z.modulus() < RADIUS && counter < MAX_ITERATIONS){
            Z.multiply(Z);
            Z.add(C);
            counter ++;
        }
        return counter;
    }

    /**
     * This function determines the colour of a given pixel.
     * If the number of iterations os equal to the MAX_ITERATIONS it colours the pixel midnightblue.
     * Otherwise it colours the pixel on a gradient determined by the number of iterations
     * it went through before  the modulus  of Z_n was greater than 2.
     *
     * @param numIter The number of iterations before the modulus of Z_n was greater than 2.
     * @return the Color of the pixel.
     */
    private Color pickColor(int numIter, int minIter) {
        Color c;
        if(numIter < 20){
            c = Color.rgb(200 ,25 + (5*numIter) , 200 + numIter);
        }
        else if (numIter < MAX_ITERATIONS) {
            int bDiff = numIter *  25/(MAX_ITERATIONS + minIter);
            int gDiff = numIter * 145/(MAX_ITERATIONS + minIter);
            c = Color.rgb(200, 110 + gDiff, 220 + bDiff);
        }
        else {
            c = Color.MIDNIGHTBLUE;
        }
        return c;
    }

    /**
     * Performs the Mandelbrot set within and area of the complex plain determined
     * by the xCorner, yCorner and length of the viewable area. It then writes this image to the GUI.
     */
    private void mandelbrot(){
        PixelWriter px = wImg.getPixelWriter();
        Complex c;
        int iter;
        int minIter = 0;
        int pic[][] = new int[SIZE][SIZE];
        for(int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                c = new Complex((xCorner + x*(length/SIZE)), (yCorner - y*(length/SIZE)));
                iter = doIterations(c);
                if(iter < minIter){
                    minIter = iter;
                }
                pic[x][y] = iter;
            }
        }
        for(int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                iter = pic[x][y];
                Color col = pickColor(iter, minIter);
                px.setColor( x, y, col);
            }
        }
    }
}
