package de.smoodi.projectchip.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Config {

    private String mcdbhost;
    private String mcdbname;
    private String mcdbuser;
    private String mcdbpw;
    private int mcdbport;

    private String token;

    private String coredbhost;
    private String coredbname;
    private String coredbuser;
    private String coredbpw;
    private int coredbport;

    public Config() {
    }

    public boolean load(String filename) throws FileNotFoundException {

        File cfgFile = new File(filename);
        if(!cfgFile.exists()) return false;
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(cfgFile);

        Map<String, Object> obj = (Map<String, Object>) yaml.load(inputStream);

        token = (String) obj.get("token");
        mcdbhost = (String) obj.get("whitelistDBHost");
        mcdbname = (String) obj.get("whitelistDBName");
        mcdbport = (int) obj.get("whitelistDBPort");
        mcdbuser = (String) obj.get("whitelistDBUser");
        mcdbpw = (String) obj.get("whitelistDBPassword");

        coredbhost = (String) obj.get("primaryDBHost");
        coredbname = (String) obj.get("primaryDBName");
        coredbport = (int) obj.get("primaryDBPort");
        coredbuser = (String) obj.get("primaryDBUser");
        coredbpw = (String) obj.get("primaryDBPassword");

        return true;

    }

    public String getMcdbhost() {
        return mcdbhost;
    }

    public String getMcdbname() {
        return mcdbname;
    }

    public String getMcdbuser() {
        return mcdbuser;
    }

    public String getMcdbpw() {
        return mcdbpw;
    }

    public int getMcdbport() {
        return mcdbport;
    }

    public String getToken() {
        return token;
    }

    public String getCoredbhost() {
        return coredbhost;
    }

    public String getCoredbname() {
        return coredbname;
    }

    public String getCoredbuser() {
        return coredbuser;
    }

    public String getCoredbpw() {
        return coredbpw;
    }

    public int getCoredbport() {
        return coredbport;
    }
}
