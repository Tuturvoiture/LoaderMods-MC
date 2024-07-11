package fr.deschamps.gestionmod_mc_2.Controller;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class GM_Controller {

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

}
