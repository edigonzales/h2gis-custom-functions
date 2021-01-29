package ch.so.agi.h2.functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import ch.ehi.ili2db.base.Ili2db;
import ch.ehi.ili2db.base.Ili2dbException;
import ch.ehi.ili2db.gui.Config;
import ch.ehi.ili2h2gis.H2gisMain;

public class RemoveInnerRingsTest {
    @Test
    public void removeInnerRings_Ok() throws IOException, Ili2dbException {
        File origDbFile = new File("./src/test/data/bauzonengrenzen.mv.db");
        File copyDbFile = new File("./src/test/data/bauzonengrenzen_copy.mv.db");
        FileUtils.copyFile(origDbFile, copyDbFile, false);

        String dburl = "jdbc:h2:"+copyDbFile.getAbsolutePath().subSequence(0, copyDbFile.getAbsolutePath().length()-6);
        try (Connection c = DriverManager.getConnection(dburl, "", "");
                Statement s = c.createStatement();)
            {
                s.execute("CREATE ALIAS IF NOT EXISTS SOGIS_RemoveInnerRings FOR \"ch.so.agi.h2.functions.RemoveInnerRings.removeInnerRings\";");
                //s.execute("SELECT SOGIS_RemoveInnerRings(GEOMETRIE, 1.0) FROM BAUZONENGRENZEN_BAUZONENGRENZE;");
                s.executeUpdate("UPDATE BAUZONENGRENZEN_BAUZONENGRENZE SET geometrie = SOGIS_RemoveInnerRings(GEOMETRIE, 1.0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Config settings = new Config();
        new H2gisMain().initConfig(settings);
        settings.setFunction(Config.FC_EXPORT);
        settings.setModels("SO_ARP_Bauzonengrenzen_20210120");
        settings.setModeldir(Paths.get("./src/test/data/").toFile().getAbsolutePath()+";"+"http://models.geo.admin.ch");
        settings.setDbfile(dburl.substring(8));
        settings.setValidation(true);
        settings.setItfTransferfile(false);
        settings.setDburl(dburl);
        String xtfFileName = new File("./src/test/data/bauzonengrenzen_fixed.xtf").getAbsolutePath();
        settings.setXtffile(xtfFileName);
        Ili2db.run(settings, null);
        System.out.println("Hallo Welt.");
        
    }

}
