package animation;

import javafx.animation.Timeline;
import javafx.scene.Node;

public abstract class AnimationFX {
    private Timeline timeLine;
    private Node node;

    public AnimationFX(Node node) {
        setNode(node);
    }

    private void setNode(Node node){
        this.node = node;
        initTimeLine();
    };

    abstract void initTimeLine();

    public Node getNode(){
        return node;
    }

    public void setTimeLine(Timeline timeLine){
        this.timeLine = timeLine;
    }

    public void play(){
        timeLine.play();
    }

}
