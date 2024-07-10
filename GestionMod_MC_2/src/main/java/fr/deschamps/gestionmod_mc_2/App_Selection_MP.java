package fr.deschamps.gestionmod_mc_2;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class App_Selection_MP implements Initializable {

    @FXML
    public Label welcomeText;
    @FXML
    public Label afficheDossierSelectionner;
    @FXML
    public ImageView img;

    public javafx.scene.control.ListView<String> ListView;
    public Button button1;
    public Button button2;
    public String dossierSelectionner;
    static String User = System.getProperty("user.name");
    static String lienDossierMods = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods\\";
    static String lienDossierModsLoader = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods_loaders\\";
    private ActionEvent event;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualiseList();
        //Image image = new Image(getClass().getResource("/fr/deschamps/gestionmod_mc_2/images/giphy3.gif").toString());img.setImage(image);
        button2.setDisable(true);
        System.out.println("User : "+User);
    }

    public static void copy(File src, File dest) throws IOException {
        GM_Controller.copyFiles(src, dest);
    }

    public static void delete(String abc) {
        GM_Controller.deleteFiles(abc);
    }

    public void switchToAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Accueil.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }

    public void deplaceFichier(ActionEvent event) {
        JFrame jFrame = new JFrame();
        int result = JOptionPane.showConfirmDialog(jFrame, "<html>Voulez-vous importer le ModPack '"+dossierSelectionner+"' ?<html><html><font color='red'><br>(Cela entraînera la suppression du répertoire mods.)</font></html>");

        if (result == 0) {
            delete(lienDossierMods);
            this.event = event;
            File src = new File(lienDossierModsLoader + dossierSelectionner);
            File dest = new File(lienDossierMods);
            try {
                copy(src, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(jFrame, "Fait !");
        }
    }

    public void listViewSelectedItem(){
        Number temp;
        dossierSelectionner = ListView.getSelectionModel().getSelectedItem();
        temp = dossierSelectionner.length();
        dossierSelectionner = dossierSelectionner.substring(10, (Integer) temp);
        afficheDossierSelectionner.setText("Dossier Selectionné : "+dossierSelectionner);
        button2.setDisable(false);
    }

    public void actualiseList() {
        ArrayList<String> listeTest = new ArrayList<String>();
        File file = new File(lienDossierModsLoader); File[] files = file.listFiles();
        if (files != null) {
            welcomeText.setText("Voila les Dossiers ModPack trouvé(s)");
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() == true) {
                    listeTest.add("Dossier : " + files[i].getName());
                } //else {listeTest.add("Fichier : " + files[i].getName());}
            }
        }
        else {
            welcomeText.setText("Le repertoire 'mods_loaders' n'existe pas ou ne contient aucun ModPack !");
        }
        ListView.getItems().clear();
        ListView.getItems().addAll(listeTest);
        button2.setDisable(true);
    }

}
