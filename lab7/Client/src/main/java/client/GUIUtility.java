package client;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUIUtility {
    public static boolean checkExceptionLogin(String email, String password) {
        if (!isValidEmailAddress(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Dialog");
            alert.setHeaderText("Email error");
            alert.setContentText("Wrong format email");
            alert.showAndWait();
            return false;
        }
        return isValidPassword(password);
    }

    public static boolean isValidPassword(String password) {
        if (password.length()<6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Dialog");
            alert.setHeaderText("Length error");
            alert.setContentText("Password need to larger than 6 chars");
            alert.showAndWait();
            return false;
        }
        if (password.contains("-") || password.contains(",") || password.contains("/") || password.contains(" ")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Dialog");
            alert.setHeaderText("Character error");
            alert.setContentText("Password can't contain character \"-\", \",\", \"/\", \" \".");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static void throwException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getClass().getName());
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    /**
     *
     * @param caller
     * @param resource
     * @param axis positive for X, negative for Y
     * @param miliStep
     */
    public static void switchAnimation(Pane caller, String resource, Integer axis, Integer miliStep, Interpolator interpolator, String mode) throws IOException {
        if (!ClientGUI.currentMode.equals(mode)) {
            ClientGUI.currentMode = mode;
            if (mode.equals("Dark mode")) caller.getScene().getStylesheets().add(ClientGUI.class.getResource("/dark-theme.css").toString());
            else caller.getScene().getStylesheets().remove(ClientGUI.class.getResource("/dark-theme.css").toString());
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClientGUI.class.getResource("/"+resource));
        loader.setResources(ResourceBundle.getBundle("bundle", new Locale(ClientGUI.currentLanguage.split("_",2)[0],ClientGUI.currentLanguage.split("_",2)[1])));
        Parent root = loader.load();
        Scene scene = caller.getScene();
        root.toFront();

        if (axis>0) root.translateXProperty().set((miliStep>0?-1:1)*scene.getWidth());
        else root.translateYProperty().set((miliStep>0?-1:1)*scene.getHeight());
        Pane pane = (Pane) scene.getRoot();
        pane.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv;
        if (axis>0) kv = new KeyValue(root.translateXProperty(),0,interpolator);
        else kv = new KeyValue(root.translateYProperty(),0,interpolator);
        KeyFrame kf = new KeyFrame(Duration.millis(Math.abs(miliStep)), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event->{
            pane.getChildren().remove(caller);
        });
        timeline.play();
    }

    public static void relocateAnimation(Pane caller, Pane waiter, Integer miliStep, Interpolator interpolator) throws IOException {
        Scene scene = caller.getScene();

        waiter.toFront();
        waiter.translateXProperty().set(scene.getWidth());
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(caller.translateXProperty(),scene.getWidth(),interpolator);
        KeyValue kv2 = new KeyValue(waiter.translateXProperty(),830,interpolator);
        KeyFrame kf = new KeyFrame(Duration.millis(Math.abs(miliStep)), kv);
        KeyFrame kf2 = new KeyFrame(Duration.millis(Math.abs(miliStep)), kv2);
        timeline.getKeyFrames().addAll(kf,kf2);
        timeline.play();
    }

    public static void login(String email, int id) throws IOException {
        Connection.getInstance().setID(id);
        ClientGUI.email = email;
        ClientGUI.stage.close();
        ClientGUI.stage = new Stage();
        ClientGUI.stage.setTitle("Main window");
        ClientGUI.stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClientGUI.class.getResource("/main.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundle", new Locale(ClientGUI.currentLanguage.split("_",2)[0],ClientGUI.currentLanguage.split("_",2)[1])));
        ClientGUI.stage.setScene(new Scene(loader.load()));
        if (ClientGUI.currentMode.equals("Dark mode")) ClientGUI.stage.getScene().getStylesheets().add(ClientGUI.class.getResource("/dark-theme.css").toString());
        ClientGUI.stage.show();
    }

}