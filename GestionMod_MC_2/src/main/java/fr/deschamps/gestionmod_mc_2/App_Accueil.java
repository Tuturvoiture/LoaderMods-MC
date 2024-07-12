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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.File;
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
    public Label textModif;
    public Label titreTexte1;
    @FXML
    public VBox vboxMP;

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

    private void infoBulle(){
        nomMp.setTooltip(new Tooltip("Nom du ModPack actuellement \nchargée dans votre dossier /mods"));
        versionMp.setTooltip(new Tooltip("Version Modpack actuellement \nchargée dans votre dossier /mods"));
        nbModMp.setTooltip(new Tooltip("Nombre de Mods chargée depuis le\nModpack dans votre dossier /mods"));
        dateCreationMp.setTooltip(new Tooltip("Date quand le Modpack \nactuel a été créée"));
    }


    private void afficheCharger() {
        List<String> info = GM_Controller.recupInfo();
        Integer nbModActuel = GM_Controller.countFiles();
        System.out.println("nbModActuel : "+nbModActuel);
        if (info.size() == 0) {
            vboxMP.setVisible(false);
            titreTexte1.setVisible(true);

        }else {
            vboxMP.setVisible(true);
            titreTexte1.setVisible(false);
            nomMp.setText(info.get(0));
            versionMp.setText(info.get(1));
            if (nbModActuel != Integer.parseInt(info.get(3))){
                nbModMp.setText(info.get(3)+" + "+(nbModActuel-Integer.parseInt(info.get(3))));
                textModif.setVisible(true);
            }else{
                nbModMp.setText(info.get(3));
            }
            dateCreationMp.setText(info.get(2));
            infoBulle();
            nbModMp.setText(info.get(3));
        }
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
