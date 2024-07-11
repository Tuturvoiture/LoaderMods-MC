package fr.deschamps.gestionmod_mc_2;

import fr.deschamps.gestionmod_mc_2.Controller.GM_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class App_Accueil implements Initializable {

    @FXML
    public Label nomMp;
    public Label versionMp;
    public Label nbModMp;
    public Label dateCreationMp;
    public Label titreTexte;

    @FXML
    public Button button1;
    public Button button2;

    @FXML
    public ImageView img;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Image image = new Image(getClass().getResource("/fr/deschamps/gestionmod_mc_2/images/giphy1.gif").toString());img.setImage(image);
        afficheCharger();
    }

    private void afficheCharger() {
        List<String> info = GM_Controller.recupInfo();
        nomMp.setText(info.get(0));
        versionMp.setText(info.get(1));
        nbModMp.setText(info.get(2));
        dateCreationMp.setText(info.get(3));
    }

    public void switchToPage1(ActionEvent event) throws IOException {
        button1.setDisable(true);
        button2.setDisable(true);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Page1.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Chargement ModPack");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPage2(ActionEvent event) throws IOException {
        button1.setDisable(true);
        button2.setDisable(true);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Page2.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Création ModPack");
        stage.setScene(scene);
        stage.show();
    }

}
