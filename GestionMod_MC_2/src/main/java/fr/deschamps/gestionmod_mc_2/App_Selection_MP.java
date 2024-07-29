package fr.deschamps.gestionmod_mc_2;

import fr.deschamps.gestionmod_mc_2.Controller.GM_Controller;
import javafx.concurrent.Task;
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
    @FXML
    public ProgressBar progressBar = new ProgressBar();

    public javafx.scene.control.ListView<String> ListView;
    public Button button1, button2, buttonAccueil;
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

    public static void copy(File src, File dest,boolean aaa) throws IOException {
        GM_Controller.copyFiles(src, dest,aaa);
    }

    public static void delete(String abc) {
        GM_Controller.deleteFiles(abc);
    }

    public void switchToAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Accueil.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("ModLoader - Accueil");
        stage.setScene(scene);
        stage.show();
    }

    private void deplaceFichier(ActionEvent event) {
        delete(lienDossierMods);
        this.event = event;
        File src = new File(lienDossierModsLoader + dossierSelectionner);
        File dest = new File(lienDossierMods);
        try {
            copy(src, dest, true);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void chargerButton(ActionEvent event) {
        int result = JOptionPane.showConfirmDialog(null, "<html>Voulez-vous importer le ModPack '"+dossierSelectionner+"' ?<html><html><font color='red'><br>(Cela entraînera la suppression du répertoire mods.)</font></html>");

        if (result == 0) {
            buttonAccueil.setDisable(true);
            button1.setDisable(true);
            button2.setDisable(true);
            ListView.setDisable(true);

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    deplaceFichier(event);
                    return null;
                }
            };

            //affiche la barre + attribut la progression à la "task"
            progressBar.setVisible(true);
            progressBar.progressProperty().bind(task.progressProperty());

            //démarrer la tache dans un nouveau thread
            new Thread(task).start();

            task.setOnSucceeded(e -> {
                // Affiche un message de confirmation
                JOptionPane.showMessageDialog(null, "Modpack importé avec succès !");

                buttonAccueil.setDisable(false);
                button1.setDisable(false);
                ListView.setDisable(false);

                // Réinitialise la barre de progression
                progressBar.setVisible(false);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
            });
        }
    }


}
