



public class Variables {

    private static boolean mac = true;
    private static String pathSep = "\\";
    //private static String ResultsFileFolder = "/Users/authorX.surnameX/Researches/MyDirectoryX/AQuestGO/Result/";
    //final static String ResultsFileFolder = "D:\\Research\\MyDirectoryX\\AQuestGO\\Result\\";
    //final static String OntologiesFileFolder = "D:\\Research\\MyDirectoryX\\AQuestGO\\Ontologies\\";
    //sprivate static String OntologiesFileFolder = "/Users/authorX.surnameX/Researches/MyDirectoryX/AQuestGO/Ontologies/";

    public static void macComputer(boolean value) {
        mac = value;
        if (mac) {
            pathSep = "/";
        }
    }

    public static String getPathSep() {
        return pathSep;
    }

    public static boolean isMacComputer() {
        return mac;
    }
    public static String getResultsFileFolder() {
        String pathSep = Variables.getPathSep();
        if (!mac) {
            return "D:"+pathSep+"Research"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"Result"+pathSep;
        }
        else {
            return ""+pathSep+"Users"+pathSep+"authorX.surnameX"+pathSep+"Researches"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"Result"+pathSep+"";
        }
    }
    public static String getOntologiesFileFolder() {
        String pathSep = Variables.getPathSep();
        if (!mac) {
            return "D:"+pathSep+"Research"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"Ontologies"+pathSep;
        }
        else {
            return ""+pathSep+"Users"+pathSep+"authorX.surnameX"+pathSep+"Researches"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"Ontologies"+pathSep+"";
        }
    }

    public static String getTemplateFilename(){
        String pathSep = Variables.getPathSep();
        if (!mac) {
            return "D:"+pathSep+"Research"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"Templates"+pathSep+"templates_complex_2.txt";
        }
        else {
            return ""+pathSep+"Users"+pathSep+"authorX.surnameX"+pathSep+"Researches"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"Templates"+pathSep+"templates_complex_2.txt";
        }
    }
    public static String getUncountableNounsFilename() { // probably we do not need that at the end :)
        String pathSep = Variables.getPathSep();
        if (!mac) {
            return "D:"+pathSep+"Research"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"OtherResources"+pathSep+"uncountableNouns.txt";
        }
        else {
            return ""+pathSep+"Users"+pathSep+"authorX.surnameX"+pathSep+"Researches"+pathSep+"MyDirectoryX"+pathSep+"AQuestGO"+pathSep+"OtherResources"+pathSep+"uncountableNouns.txt";
        }
    }
}
