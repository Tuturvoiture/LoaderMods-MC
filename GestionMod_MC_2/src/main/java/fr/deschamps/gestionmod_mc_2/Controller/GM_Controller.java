package fr.deschamps.gestionmod_mc_2.Controller;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;


public class GM_Controller {


    static String User = System.getProperty("user.name");
    static String lienDossierMods = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods\\";
    static String lienDossierModsLoader = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods_loaders\\";

    public static void copyFiles(File src, File dest,boolean copiCachee) throws IOException {
        if (src.isDirectory()) {
            // Vérifiez si le répertoire est caché et si nous devons le copier ou non
            if (src.getName().startsWith(".") && !copiCachee) {
                //System.out.println("Skipping hidden folder: " + src);
                return;
            }

            // si le répertoire n'existe pas, créez-le
            if (!dest.exists()) {
                dest.mkdir();
                //System.out.println("Dossier " + src + "  > " + dest);
            }

            // lister le contenu du répertoire
            String[] files = src.list();

            for (String file : files) {
                // construire la structure des fichiers src et dest
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // copie récursive
                copyFiles(srcFile, destFile,copiCachee);
            }
        } else {
            // si src est un fichier, copiez-le
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int length;
            // copier le contenu du fichier
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            System.out.println("Fichier " + src + " > " + dest);
        }
    }

    public static void deleteFiles(String abc) {
        File directory = new File(abc);

        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    public static void deleteFilesByExtension(String directoryPath, String extension) throws IOException {
        Path startPath = Paths.get(directoryPath);
        System.out.println(directoryPath);
        if (!Files.exists(startPath) || !Files.isDirectory(startPath)) {
            System.err.println("Le chemin spécifié n'existe pas ou n'est pas un répertoire : " + directoryPath);
            return;
        }

        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(extension)) {
                    try {
                        Files.delete(file);
                        System.out.println("Supprimé : " + file.toString());
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la suppression du fichier : " + file.toString());
                        e.printStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.err.println("Impossible de visiter le fichier : " + file.toString());
                exc.printStackTrace();
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static String findFilesWithExtension(File dir, String extension) {
        if (!dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (file.getName().endsWith(extension)) {
                return file.getName();
            }
        }
        return null;
    }

    public static List<String> recupInfo() {
        List<String> liste = new ArrayList<>();
        String nomFichier = findFilesWithExtension(new File(lienDossierMods), ".confml");
        if (nomFichier == null) {
            return liste;
        }
        try {
            // Création d'un fileReader pour lire le fichier
            FileReader fileReader = new FileReader(lienDossierMods + File.separator + nomFichier);

            // Création d'un bufferedReader qui utilise le fileReader
            BufferedReader reader = new BufferedReader(fileReader);

            // Lecture des lignes et ajout à la liste
            String line = reader.readLine();
            while (line != null) {
                liste.add(line);
                line = reader.readLine();
            }
            reader.close();

            // Vérification des informations requises
            if (liste.size() < 4 || liste.get(0).isEmpty() || liste.get(1).isEmpty() || liste.get(2).isEmpty() || liste.get(3).isEmpty()) {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return liste;
    }

    public static int countFilesWithExtension() {
        File dir = new File(lienDossierMods);
        File[] files = dir.listFiles();
        int count = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void creerFichier(String nom, String version,Boolean siLoader) {

        //récuperer info, nom, version, date du jour, nombre de mods, utilisateur actuel
        //String nom = nomMP.getText();
        //String version = (String) choiceBox.getValue();
        String DATE_FORMAT = "dd/MM/yyyy";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        int nbMods = countFilesWithExtension();
        String user = User;
        List<String> lignes = Arrays.asList(nom, version, sdf.format(date), Integer.toString(nbMods), user);
        File file;
        if (siLoader){
            file = new File(lienDossierModsLoader + nom + "\\"+nom+".confml");
        }
        else {
            file = new File(lienDossierMods +nom+".confml");
        }
        try {

            if (file.createNewFile()) {
                //System.out.println("Fichier créé : " + file.getName());
                Files.write(file.toPath(), lignes, StandardCharsets.UTF_8);
            } else {
                //System.out.println("Fichier déjà existant.");
            }
        } catch (IOException e) {
            //System.out.println("Erreur lors de la création du fichier.");
            e.printStackTrace();
        }
    }


}
