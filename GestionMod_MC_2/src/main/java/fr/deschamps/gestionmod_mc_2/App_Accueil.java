package fr.deschamps.gestionmod_mc_2;

import fr.deschamps.gestionmod_mc_2.Controller.GM_Controller;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.layout.Pane;
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
    public Label nomMp, versionMp, nbModMp, dateCreationMp, titreTexte, textModif, titreTexte1;
    @FXML
    public VBox vboxMP;
    @FXML
    public Button button1, button2, button3;
    @FXML
    public ImageView img, loading;
    @FXML
    public Pane paneLoading;

    List<String> info = GM_Controller.recupInfo();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Image image = new Image(getClass().getResource("/fr/deschamps/gestionmod_mc_2/images/giphy1.gif").toString());img.setImage(image);
        Image loadingImage = new Image(getClass().getResource("/fr/deschamps/gestionmod_mc_2/images/loading.gif").toString());loading.setImage(loadingImage);
        afficheCharger();
    }

    private void infoBulle(boolean aaa) {
        nomMp.setTooltip(new Tooltip("Nom du ModPack actuellement \nchargé dans votre dossier /mods"));
        versionMp.setTooltip(new Tooltip("Version des mods actuellement \nchargées depuis le Modpack"));
        dateCreationMp.setTooltip(new Tooltip("Date de création du Modpack chargée\nactuellement dans votre dossier /mods"));
        if(aaa){
            nbModMp.setTooltip(new Tooltip("!! Attention, le nombre de mods trouvée est différent de celui du Modpack actuel !!\nLe Pack actuel devrait contenir un total de "+info.get(3)+" mods"));
            textModif.setTooltip(new Tooltip("Des modifications externe au logiciel ont été apportées au\nModpack actuellement chargée dans votre dossier /mods."));
        }else {
            nbModMp.setTooltip(new Tooltip("Nombre de Mods chargée depuis le\nModpack dans votre dossier /mods"));
        }
    }

    private void afficheCharger() {
        Integer nbModActuel = GM_Controller.countFilesWithExtension();
        boolean changement = false;
        if (info.isEmpty()) {
            titreTexte.setVisible(false);
            vboxMP.setVisible(false);
            titreTexte1.setVisible(true);
        }else {
            Integer abc = Integer.parseInt(info.get(3));
            titreTexte.setVisible(true);
            vboxMP.setVisible(true);
            titreTexte1.setVisible(false);
            nomMp.setText(info.get(0));
            versionMp.setText(info.get(1));
            if (!nbModActuel.equals(abc)){
                //changer couleur + texte
                nbModMp.setStyle("-fx-text-fill: #9c0e02;");
                nbModMp.setText(nbModActuel.toString()+"*");
                textModif.setVisible(true);
                changement = true;
            }else{
                nbModMp.setText(info.get(3));
            }
            dateCreationMp.setText(info.get(2));
            infoBulle(changement);
        }
    }

    public void switchToPage1(ActionEvent event) throws IOException {
        tache("Chargement ModPack", "Select", event);
    }

    public void switchToPage2(ActionEvent event) throws IOException {
        tache("Création ModPack", "Create", event);
    }

    public void switchToPage3(ActionEvent event) throws IOException {
        tache("Mise à jour ModPack", "MAJ", event);
    }

    private void tache(String titre, String fichier, ActionEvent event) {
        // Afficher l'animation de chargement et désactiver les boutons
        loading.setVisible(true);
        paneLoading.setVisible(true);
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);


        // Démarrer la tâche en arrière-plan
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fichier + ".fxml")));

                // Manipulation de l'UI après chargement de la nouvelle scène
                Platform.runLater(() -> {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("ModLoader - " + titre);
                    stage.setScene(scene);
                    stage.show();

                    // Réactiver les boutons et cacher l'animation de chargement
                    button1.setDisable(false);
                    button2.setDisable(false);
                    button3.setDisable(false);
                    loading.setVisible(false);
                    paneLoading.setVisible(false);
                });
                return null;
            }
        };

        // Démarrer le thread de la tâche
        new Thread(task).start();
    }


}
