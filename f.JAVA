import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class ColorDetectionFX extends Application {

    private static final Scalar GREEN_LOWER = new Scalar(73, 104, 77);
    private static final Scalar GREEN_UPPER = new Scalar(90, 200, 199);
    private static final Scalar BLUE_LOWER = new Scalar(98, 134, 45);
    private static final Scalar BLUE_UPPER = new Scalar(116, 214, 162);

    private VideoCapture videoCapture;
    private ImageView imageView;

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        videoCapture = new VideoCapture(0);
        videoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, 800);
        videoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 600);

        imageView = new ImageView();
        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Color Detection with JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            videoCapture.release();
            System.exit(0);
        });

        processFrames();
    }

    private void processFrames() {
        Mat frame = new Mat();
        while (true) {
            if (videoCapture.read(frame)) {
                processFrame(frame);
            } else {
                System.out.println("Error: Cannot read frame.");
                break;
            }
        }
    }

    private void processFrame(Mat frame) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);

        Mat greenMask = new Mat();
        Core.inRange(hsv, GREEN_LOWER, GREEN_UPPER, greenMask);

        Mat blueMask = new Mat();
        Core.inRange(hsv, BLUE_LOWER, BLUE_UPPER, blueMask);

        Image imageToShow = convertMatToImage(frame);
        updateImageView(imageToShow);

        processMouseActions(greenMask, blueMask);
    }

    private void processMouseActions(Mat greenMask, Mat blueMask) {
        // Implement mouse actions here based on the detected colors (greenMask and blueMask)
        // You can use the Robot class to control the mouse, similar to the previous example.
        // Example: Robot.mouseMove(x, y);
    }

    private Image convertMatToImage(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    private void updateImageView(Image image) {
        imageView.setImage(image);
    }
}
