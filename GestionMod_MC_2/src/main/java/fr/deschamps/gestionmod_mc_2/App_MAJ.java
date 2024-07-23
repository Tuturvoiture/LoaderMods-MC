package fr.deschamps.gestionmod_mc_2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import fr.deschamps.gestionmod_mc_2.Controller.GM_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App_MAJ implements Initializable {

    @FXML
    public Label welcomeText, afficheDossierSelectionner;
    @FXML
    public ImageView img;
    @FXML
    public Pane paneErreur;
    @FXML
    public ProgressBar progressBar = new ProgressBar();

    public javafx.scene.control.ListView<String> ListView;
    List<String> info = GM_Controller.recupInfo();

    public Button button1, button2, button3, buttonAccueil;
    static String User = System.getProperty("user.name");
    static String lienDossierMods = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods\\";
    static String lienDossierModsLoader = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods_loaders\\";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ajouter image
        actualiseList();
        checkMPDossier();
        progressBar.setVisible(false);
    }

    public void switchToAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Accueil.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Accueil");
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


    public void actualiseList() {
        ArrayList<String> listeTest = new ArrayList<String>();
        File file = new File(lienDossierMods); File[] files = file.listFiles();
        if (files != null) {
            if (files.length > 1){
                welcomeText.setText("Voila les Mods trouvés");
            }else {
                welcomeText.setText("Voila le Mod trouvé");
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() == true) {
                    listeTest.add("File : " + files[i].getName());
                }
            }

        }
        else {
            welcomeText.setText("Répertoire vide ou inexistant !");
        }
        ListView.getItems().clear();
        ListView.getItems().addAll(listeTest);
    }

    private void checkMPDossier(){
        String nom = info.get(0);
        if (!chercheDossierAvecNom(nom)){
            paneErreur.setVisible(true);
        }
    }

    private boolean chercheDossierAvecNom(String nom) {
        ArrayList<String> listeTest = new ArrayList<String>();
        File file = new File(lienDossierModsLoader); File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() == true) {
                    if (files[i].getName().equals(nom)){return true;}
                }
            }

        }
        return false;
    }


}
