package de.smoodi.projectchip.util;

import java.util.Random;

public class Util {

    /**
     * Returns a fancy random title for the help command.
     * @return
     */
    public static String randomTitleHelp(){
        switch(new Random().nextInt(6)) {
            case 0:
                return "Uuuh, it's dangerous to go alone... Take this info.";
            case 1:
                return "Did not expect to see you so soon..";
            case 2:
                return "There's something wrong here... I can feel it.";
            case 3:
                return "HELP, HELP, HELP - SOMEONE NEEDS ASSISTANCE!";
            case 4:
                return ":thinking:";
            case 5:
                return "42 ... Wait that's not what you were asking for?";
            default:
                return "Help page";
        }
    }

    public static String removeInvalidCharacters(String str) {
        return str.replaceAll("'", "").replaceAll("\"", "");
    }
}
