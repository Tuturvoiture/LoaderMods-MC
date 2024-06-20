module fr.deschamps.gestionmod_mc_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens fr.deschamps.gestionmod_mc_2 to javafx.fxml;
    exports fr.deschamps.gestionmod_mc_2;
    exports fr.deschamps.gestionmod_mc_2.Controller;
    opens fr.deschamps.gestionmod_mc_2.Controller to javafx.fxml;
}