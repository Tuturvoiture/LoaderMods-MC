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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import javafx.scene.Group;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static fr.deschamps.gestionmod_mc_2.App_Selection_MP.copy;

public class App_Creation_MP implements Initializable {

    @FXML
    public Label welcomeText;
    @FXML
    public Label afficheDossierSelectionner;
    @FXML
    public TextField nomMP;
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
        Image image = new Image(getClass().getResource("/fr/deschamps/gestionmod_mc_2/images/giphy2.gif").toString());
        img.setImage(image);
        System.out.println("User : "+User);
        System.out.println(lienDossierModsLoader);
    }

    public void switchToAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Accueil.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }

    public void actualiseList() {
        ArrayList<String> listeTest = new ArrayList<String>();
        File file = new File(lienDossierMods); File[] files = file.listFiles();
        if (files != null) {
            welcomeText.setText("Voila les fichiers Mods trouvé(s)");
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() == true) {
                    listeTest.add("File : " + files[i].getName());
                } //else {listeTest.add("Fichier : " + files[i].getName());}
            }
        }
        else {
            welcomeText.setText("Répertoire vide ou inexistant !");
        }
        ListView.getItems().clear();
        ListView.getItems().addAll(listeTest);
    }

    public void listViewSelectedItem(){
        Number temp;
        dossierSelectionner = ListView.getSelectionModel().getSelectedItem();
        temp = dossierSelectionner.length();
        dossierSelectionner = dossierSelectionner.substring(7, (Integer) temp);
        afficheDossierSelectionner.setText(dossierSelectionner);
    }


    //crée un dossier mods_loader avec nom
    private void creerDossierModsLoader() {
        if (nomMP.getText().equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "<html><font color='red'>Erreur : Nom du ModPack vide</font></html>");
        }else{
            File file = new File(lienDossierModsLoader + nomMP.getText());
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Dossier "+nomMP.getText()+" créé");
                } else {
                    System.out.println("Erreur lors de la création du dossier "+nomMP.getText());
                }
            }
        }
    }

    //copie dossier mods dans le dossier nouveau dossier mods_loader
    public void creationNouveauMP(ActionEvent event) {
        JFrame jFrame = new JFrame();
        int result = JOptionPane.showConfirmDialog(jFrame, "Voulez-vous créer un nouveau ModPack avec les fichiers présent dans mods ?");
        if (result == 0) {
            if (nomMP.getText().equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "<html><font color='red'>Erreur : Nom du ModPack vide</font></html>");
            }else {
                creerDossierModsLoader();
                this.event = event;
                File src = new File(lienDossierMods);
                File dest = new File(lienDossierModsLoader + nomMP.getText());
                try {
                    copy(src, dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(jFrame, "Fait !");
                nomMP.clear();
            }

        }
    }





}
