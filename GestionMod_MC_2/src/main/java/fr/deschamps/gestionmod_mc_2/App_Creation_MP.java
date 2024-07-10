package fr.deschamps.gestionmod_mc_2;

import fr.deschamps.gestionmod_mc_2.Controller.GM_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

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
    @FXML
    public Pane paneCreation;
    @FXML
    public ProgressBar progressBar = new ProgressBar();;



    public javafx.scene.control.ListView<String> ListView;


    public Button button1;
    public Button button2;
    public Button validerButton;
    public Button annulerButton;
    public ChoiceBox choiceBox;
    public VBox vBox1;
    public String dossierSelectionner;
    static String User = System.getProperty("user.name");
    static String lienDossierMods = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods\\";
    static String lienDossierModsLoader = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods_loaders\\";
    private ActionEvent event;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualiseList();
        Image image = new Image(getClass().getResource("/fr/deschamps/gestionmod_mc_2/images/giphy2.gif").toString());img.setImage(image);
        System.out.println("User : "+User);
        System.out.println(lienDossierModsLoader);
        paneCreation.setVisible(false);
        //set version dans choiceBox
        choiceBox.getItems().addAll("1.20.1","1.19.1","1.18.1","1.17.1","1.16.5","1.15.2","1.14.4","1.13.2","1.12.2","1.11.2","1.10.2","1.9.4","1.8.9","1.7.10");
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
            if (files.length > 1){
                welcomeText.setText("Voila les Mods trouvés");
            }else {
                welcomeText.setText("Voila le Mod trouvé");
            }
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

    //crea fichier texte avec info du ModPack
    private void creerFichier(){

        //récuperer info, nom, version, date du jour, nombre de mods, utilisateur actuel
        String nom = this.nomMP.getText();
        String version = (String) choiceBox.getValue();
        Date date = new Date();
        int nbMods = ListView.getItems().size();
        String user = User;
        List<String> lignes = Arrays.asList(nom, version, date.toString(), Integer.toString(nbMods), user);
        try {
            File file = new File(lienDossierModsLoader + nom + "\\"+nom+".confml");
            if (file.createNewFile()) {
                System.out.println("Fichier créé : " + file.getName());
                Files.write(file.toPath(), lignes, StandardCharsets.UTF_8);
            } else {
                System.out.println("Fichier déjà existant.");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du fichier.");
            e.printStackTrace();
        }
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
    private void creationNouveauMP(ActionEvent event) {
        creerDossierModsLoader();
        this.event = event;
        File src = new File(lienDossierMods);
        File dest = new File(lienDossierModsLoader + nomMP.getText());
        try {
            copy(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("copi terminé");
    }

    //affiche le pane de création
    public void affichePaneCreation(ActionEvent event) {
        JFrame jFrame = new JFrame();
        paneCreation.setVisible(true);
        vBox1.setVisible(false);
    }

    //retire le pane création
    public void setAnnulerButton(ActionEvent event) {
        paneCreation.setVisible(false);
        vBox1.setVisible(true);
    }

    //valide la création du ModPack
    public void setValiderButton(ActionEvent event) {
        JFrame jFrame = new JFrame();
        int result = JOptionPane.showConfirmDialog(jFrame, "Voulez-vous créer un fichier texte avec les informations suivantes ?\nNom du ModPack : '"+nomMP.getText()+"'\nVersion du ModPack : '"+choiceBox.getValue()+"'");
        if (result == 0) {
            //verif si nom du ModPack vide
            if (nomMP.getText().equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "<html><font color='red'>Erreur : Nom du ModPack vide</font></html>");
            }else {
                //verif si version selectionné
                if (choiceBox.getValue() == null) {
                    JOptionPane.showMessageDialog(new JFrame(), "<html><font color='red'>Erreur : Version du ModPack non sélectionné</font></html>");
                }else {
                    progressBar.setVisible(true);
                    creationNouveauMP(event);
                    creerFichier();
                    JOptionPane.showMessageDialog(jFrame, "Modpack crée avec succès !");
                    paneCreation.setVisible(false);
                    vBox1.setVisible(true);
                }
            }

        }
    }
}
