package animation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Bounce extends AnimationFX {

    public Bounce(Node node) {
        super(node);
    }

    @Override
    void initTimeLine() {
        setTimeLine(new Timeline(new KeyFrame(Duration.millis(0),
                new KeyValue(getNode().opacityProperty(), 0, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                new KeyValue(getNode().scaleXProperty(), 0.3, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                new KeyValue(getNode().scaleYProperty(), 0.3, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
        ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(getNode().scaleXProperty(), 1.1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 1.1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(getNode().scaleXProperty(), 0.9, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 0.9, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(getNode().opacityProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleXProperty(), 1.03, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 1.03, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(getNode().scaleXProperty(), 0.97, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 0.97, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().opacityProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleXProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                )));
    }

}
