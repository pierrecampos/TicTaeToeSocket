package util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Utils {
    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static int[] transformIndex(int index) {
        int row, column = 0, count = 0;
        for (row = 0; row < 3; row++, count++) {
            for (column = 0; column < 2; column++, count++) {
                if (count == index) {
                    break;
                }
            }
            if (count == index) {
                break;
            }
        }
        int[] fields = new int[2];
        fields[0] = row;
        fields[1] = column;

        return fields;
    }

    public static List<Integer> transformPosition(Boolean[][] matrix) {
        int row, column = 0, position = 0;
        List<Integer> indexButtons = new ArrayList<>();
        for (row = 0; row < 3; row++) {
            for (column = 0; column < 3; column++, position++) {
                if (matrix[row][column] != null) {
                    indexButtons.add(position);
                }
            }
        }
        return indexButtons;
    }
}
