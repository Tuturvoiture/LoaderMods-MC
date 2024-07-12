package fr.deschamps.gestionmod_mc_2.Controller;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class GM_Controller {


    static String User = System.getProperty("user.name");
    static String lienDossierMods = "C:\\Users\\"+User+"\\AppData\\Roaming\\.minecraft\\mods\\";

    public static void copyFiles(File src, File dest,boolean copiCachee) throws IOException {
        if (src.isDirectory()) {
            // Vérifiez si le répertoire est caché et si nous devons le copier ou non
            if (src.getName().startsWith(".") && !copiCachee) {
                System.out.println("Skipping hidden folder: " + src);
                return;
            }

            // si le répertoire n'existe pas, créez-le
            if (!dest.exists()) {
                dest.mkdir();
                System.out.println("Dossier " + src + "  > " + dest);
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
            System.out.println(dir + " is not a directory");
            return null;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println("Could not list files in directory " + dir);
            return null;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Appel récursif pour parcourir les sous-répertoires
                String result = findFilesWithExtension(file, extension);
                if (result != null) {
                    return result; // Si un fichier est trouvé, retournez-le immédiatement
                }
            } else if (file.getName().endsWith(extension)) {
                return file.getName();
            }
        }
        return null;
    }

    public static List<String> recupInfo() {
        List<String> liste = new ArrayList<>();
        String nomFichier = findFilesWithExtension(new File(lienDossierMods), ".confml");

        if (nomFichier == null) {
            System.out.println("rien trouvé.");
            return liste;
        }

        try {
            // Création d'un fileReader pour lire le fichier
            FileReader fileReader = new FileReader(lienDossierMods + nomFichier);

            // Création d'un bufferedReader qui utilise le fileReader
            BufferedReader reader = new BufferedReader(fileReader);

            // une fonction à essayer pouvant générer une erreur
            String line = reader.readLine();

            while (line != null) {
                // lecture de la prochaine ligne
                liste.add(line);
                line = reader.readLine(); // Lisez la ligne suivante
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static int countFiles() {
        File file = new File(lienDossierMods); File[] files = file.listFiles();
        Integer nb = 0;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() == true) {
                    String result = findFilesWithExtension(file, ".comfml");
                    if (result != null) {
                        nb++;
                    }
                }
            }
        }
        return nb;
    }

}
