package scripts.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.tribot.script.sdk.Waiting;

import javax.swing.*;
import java.net.URL;

public class GuiFx extends Application {

    private Controller controller;

    private Stage stage;
    private Scene scene;

    private boolean isOpen = false;

    private URL fxml;

    public GuiFx(URL fxml) {

        this.fxml = fxml;

        // We have to start the JFX thread from the EDT otherwise tribot will end it.
        SwingUtilities.invokeLater(() -> {

            new JFXPanel(); // we have to init the toolkit

            Platform.runLater(() -> {
                try {
                    final Stage stage = new Stage();

                    start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        waitForInit();
    }


    public Scene getScene() {
        return this.scene;
    }

    public Stage getStage() {
        return this.stage;
    }

    /**
     * The main entry point for all JavaFX applications. The start method is called
     * after the init method has returned, and after the system is ready for the
     * application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage
     *            the primary stage for this application, onto which the application
     *            scene can be set. The primary stage will be embedded in the
     *            browser if the application was launched as an applet. Applications
     *            may create other stages, if needed, but they will not be primary
     *            stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        stage.setTitle("Motherlode Mine X - Written By Polymorphic");

        stage.getIcons().add(new Image("https://jacksonjohnson.ca/motherlodeminex/dragon_pickaxe_icon.png"));

        stage.setAlwaysOnTop(true);

        Platform.setImplicitExit(false);

        FXMLLoader loader = new FXMLLoader(fxml);

        loader.setClassLoader(this.getClass().getClassLoader());

        Parent box = loader.load();

        controller = loader.getController();

        controller.setGui(this);

        scene = new Scene(box);

        stage.setScene(scene);

        stage.setResizable(false);
    }

    @SuppressWarnings("unchecked")
    public <T extends Controller> T getController() {

        return (T) this.controller;

    }

    public void show() {

        if (stage == null)
            return;

        isOpen = true;

        Platform.runLater(() -> stage.show());
    }

    public void close() {

        if (stage == null)
            return;

        isOpen = false;

        Platform.runLater(() -> stage.close());
    }

    public boolean isOpen() {
        return isOpen;
    }

    private void waitForInit() {
        Waiting.waitUntil(5000, () -> stage != null);
    }
}

