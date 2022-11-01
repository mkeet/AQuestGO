/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;


public class PropertyClassification {
    private String originalNameProperty;
    private String formattedProperty;
    private String urlProperty;
    private PropertyType propertyType;
    private String oldFormattedProperty;
    private ArrayList<String> path;
    private ArrayList<String> report;
    private String[] words;
    private Verbaliser verbaliser;

    public PropertyClassification(String urlProperty, Verbaliser verbaliser) {
        this.verbaliser = verbaliser;

        this.urlProperty = urlProperty;
        //   System.out.println("urlProperty . "+ urlProperty);
        this.originalNameProperty = UtilsLinguistic.removeUrl(urlProperty);

        System.out.println("URL PROPERTY = " + urlProperty);
        //try {
        this.formattedProperty = UtilsLinguistic.stringProcess(urlProperty);
        // }
        //catch (Exception e){
        //   this.formattedProperty = urlProperty;
        //}
        this.oldFormattedProperty = this.formattedProperty;
        this.report = new ArrayList<>();
        this.path = new ArrayList<>();
        System.out.println("--->Classification1");
        this.propertyType = this.classify();
        System.out.println("--->Classification2");
        this.ArticleRectification();
        System.out.println("--->Classification3");

        this.rectifyIsOPCategoryResults();
    }


    public PropertyClassification(Verbaliser verbaliser) {
        this.verbaliser = verbaliser;
    }

    public String getOldFormattedProperty() {
        return oldFormattedProperty;
    }

    public PropertyType classifyOP(String urlProperty) {
        try {
            this.urlProperty = urlProperty;
            //   System.out.println("urlProperty . "+ urlProperty);
            this.originalNameProperty = UtilsLinguistic.removeUrl(urlProperty);

            System.out.println("URL PROPERTY = "+urlProperty);
            //try {
            this.formattedProperty = UtilsLinguistic.stringProcess(urlProperty);
            // }
            //catch (Exception e){
            //   this.formattedProperty = urlProperty;
            //}
            this.oldFormattedProperty = this.formattedProperty;
            this.report = new ArrayList<>();
            this.path = new ArrayList<>();
            System.out.println("--->Classification1");
            this.propertyType = this.classify();
            System.out.println("--->Classification2");
           // this.printReprot();
            this.ArticleRectification();
            // System.out.println("--->Classification3");
            this.rectifyIsOPCategoryResults();

        }
        catch (Exception e ){
            System.out.println("Something wrong with PropertyClassification");
            e.printStackTrace();
        }
        return this.propertyType;
    }
    public void rectifyIsOPCategoryResults() {
        System.out.println("FORMATTED__PROP = " + this.formattedProperty);
        if (this.propertyType == PropertyType.OP_IS_NOUNS_PREP
                || this.propertyType == PropertyType.OP_IS_PAST_PARTICIPLE_PREP
                || this.propertyType == PropertyType.OP_IS_PAST_PARTICIPLE_BY) {

            if (this.formattedProperty.startsWith("is ")) {
                this.formattedProperty = this.formattedProperty.substring(3);
            }
        }
        if (this.propertyType == PropertyType.OP_IS_NOUNS_PREP) {
            //this.formattedProperty = UtilsLinguistic.getRightArticle("a", this.formattedProperty) + this.formattedProperty;
            this.formattedProperty = this.verbaliser.addArticleIfNecessary("a", this.formattedProperty + "");
        }
        System.out.println("NEW__FORMATTED__PROP = " + this.formattedProperty);
    }


    public PropertyType getPropertyType() {
        return this.propertyType;
    }
    public String getInfo() {
        return this.getFormattedProperty()+ " -> "+this.getNewFormattedProperty()+ " ["+this.propertyType.toString()+"]";
    }

    private void ArticleRectification() {

        for (int i = 0; i < this.words.length-1; i++) {
            String first = this.words[i].toLowerCase();
            String second = this.words[i+1].toLowerCase();
            if (first.equals("a")||first.equals("an")) {
                String rightArticle = UtilsLinguistic.getRightArticle(first, second);
                this.words[i] = rightArticle;
            }
        }
    }

    public String getNewFormattedProperty() {
        String ret = "";
        for(String w : words ) {
            ret+=w+" ";
        }
        ret = ret.trim();
        ret = UtilsLinguistic.stringProcessNormal(ret);
        return ret;
    }

    public String getFormattedProperty() {
        return this.formattedProperty;
    }

    private void addStateToPath(int state) {
        this.path.add(""+state);
    }

    public String getPath() {
        String ret = "";
        for (int i = 0; i < this.path.size()-1; i++) {
            ret+=this.path.get(i)+ " -> ";
        }
        if (this.path.size() != 0) { // empty
            ret+= this.path.get(this.path.size()-1);
        }
        else ret = "no path";
        return ret;
    }

    private PropertyType classify()  {
        this.report = new ArrayList<String>();
        this.path = new ArrayList<>();
        PropertyType propertyType = PropertyType.OP_UNKNOWN;

        int state = 0; // initial state
        this.addStateToPath(state);

        boolean finished = false;
        this.words = this.getFormattedProperty().split(" ");
        int indice = 0;

        String word = words[indice];
        while(!finished) {
            System.out.println("State = "+state);
            switch(state) {
                case 0:
                     System.out.println("word = "+word);
                    if (word.equals("is")) {
                        state = 1;
                    }
                    else if(word.equals("has")) {
                        state = 2;
                    }
                    else if(this.verbaliser.isVerbPastParticiple(word)) {
                          System.out.println("word case 0 = "+word);
                        state = 4;
                    }
                    else if(this.verbaliser.isVerb(word)) {

                        words[indice] = this.verbaliser.getLemmaVerb(word);
                        word = words[indice];

                        System.out.println("");
                        String thirdPerson = this.verbaliser.getThirdPerson(word);
                        System.out.println("Third Person = "+ thirdPerson );
                        System.out.println("");
                        state = 3;
                    }
                    else {
                        this.report.add("state 0 : the ['"+word+"'] is not 'is' nor 'has' nor a past participle nor a verb [prediction]");
                        state = 101;
                    }

                    break;
                case 101:
                    String lastWord = words[words.length-1];
                    if(this.verbaliser.isPreposition(lastWord)) { // its probably is
                        String newFormatted = "is "+this.getFormattedProperty();
                        words = newFormatted.split(" ");

                        indice = 0;
                        state = 0;
                        word = words[0];
                    }
                    else {
                        String newFormatted = "has "+this.getFormattedProperty();
                        words = newFormatted.split(" ");
                        //indice = 0;
                        state = 2;
                    }
                    break;


                case 1:
                    indice++;
                    if (indice >= words.length) {
                        this.report.add("state 1 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }
                    else word = words[indice];

                    if(this.verbaliser.isVerbPastParticiple(word))
                        state = 4;
                    else if (this.verbaliser.isDeterminer(word)) {
                        state = 8;
                    }
                    else state = 7; // may be we have to chek if the next word is a noun or an adj/part

                    break;

                case 4:
                    indice++;

                    if (indice >= words.length) {
                        word = word+"-by";
                        words[indice-1] = word;
                        state = 5;
                    }
                    else {
                        word = words[indice];
                        if (word.equals("by"))
                            state = 5;
                        else if (this.verbaliser.isPreposition(word)) { // phrasal verb
                            state = 6;
                        }
                        else if (this.verbaliser.isAdjectiveParticiple(word)||this.verbaliser.isNoun(word)){
                            state = 41;
                        }
                        else {
                            this.report.add("state 4 : word :['"+word+"']  not by nor empty ");
                            /// ovaina daholo ny conditions d'arret rehetra + comments apiana word (mila concentration bebe kokoa :) )
                            state = 100;
                        }
                    }

                    break;
                case 41:
                    String newFormatted2 = "is a "+this.getFormattedProperty();
                    words = newFormatted2.split(" ");
                    state = 0;
                    indice = 0;
                    word = words[0];

                    break;
                case 5:
                    indice++;

                    /*if (indice >= words.length) {
                        this.report.add("state 1 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    } else word = words[indice];*/

                    if (indice >= words.length) {
                        if (!words[0].equals("is"))
                            words[0]= "is "+words[0];
                        propertyType = PropertyType.OP_IS_PAST_PARTICIPLE_BY;
                        finished = true;
                    }
                    else {
                        state = 4;
                    }
                    break;
                case 61:
                    indice++;
                    if (indice >= words.length) {
                        if (!words[0].equals("is"))
                            words[0]= "is "+words[0];
                        propertyType = PropertyType.OP_IS_PAST_PARTICIPLE_PREP;
                        finished = true;
                    }
                    else {
                        state = 4;
                    }
                    break;
                case 6:
                    if ((indice+1) >= words.length) { // processing by after preposition
                        //word = word;
                        //words[indice] = word;
                        state = 61;
                    } else{
                        indice++;
                        word = words[indice];
                        if (word.equals("by"))
                            state = 5;
                        else {
                            state = 100;
                            this.report.add("state 6 : next ['"+word+"'] is not 'by' nor empty");
                        }
                    }
                    break;

                case 7 : // eto mbola mila ketrehana hoe ahoana ilay preformatting

                    //word = "a "+word;
                    words[indice] = "a "+word;

                    String newFormatted3 = "";
                    for (int k = 0; k < words.length; k++) {
                        newFormatted3+=words[k]+ " ";
                    }
                    newFormatted3 = newFormatted3.trim();

                    words = newFormatted3.split(" ");

                    indice = 0;
                    state = 0;
                    word = words[indice];
                    /*
                    state = 8;
                    System.out.println(">>>>>>>>> state = 7 "+ word+ " indice = "+indice);
                    indice--;
                    break;*/
                    break;
                case 8:
                    indice++;
                    if (indice >= words.length) {
                        this.report.add("state 8 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if(this.verbaliser.isNoun(word)) {
                        state = 9;
                    }
                    else if (this.verbaliser.isAdjectiveParticiple(word))
                    {
                        state = 81;
                    }
                    else
                    {
                        this.report.add("state 8 : next ['"+word+"'] is not a noun nor an adj nor a participle (present/past)");
                        state = 100;
                    }
                    break;

                case 9 :
                    indice++;
                    if (state!= 10 && indice >= words.length) {
                        this.report.add("state 9 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if (this.verbaliser.isNoun(word))
                        state = 9;
                    else if (this.verbaliser.isPreposition(word)) {
                        state = 10;
                    }
                    else {
                        this.report.add("state 9 : next ['"+word+"'] is not a noun nor a preposition");
                        state = 100;
                    }


                    break;

                case 10:
                    indice++;
                    if (indice >= words.length)
                    {
                        finished = true;
                        propertyType = PropertyType.OP_IS_NOUNS_PREP;
                    }
                    else {
                        word = words[indice];
                        if (this.verbaliser.isDeterminer(word)){
                            state = 8;
                        }
                        else if (this.verbaliser.isAdjectiveParticiple(word)){
                            state = 81;
                        }
                        else if (this.verbaliser.isNoun(word)){
                            state = 9;
                        }
                        else{
                            state = 100;
                            this.report.add("state 10 : next ['"+word+"'] is not a noun nor a adj/part nor a determiner");
                        }
                    }
                    break;

                case 81:
                    indice++;
                    if (indice >= words.length) {
                        this.report.add("state 81 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if (this.verbaliser.isNoun(word)) {
                        state = 9;
                    } else if (this.verbaliser.isAdjectiveParticiple(word)){
                        state = 81;
                    }
                    else if (this.verbaliser.isPreposition(word))
                        state = 10;
                    else {
                        this.report.add("state 81 : next ['"+word+"'] is not a noun nor a adj/part");
                        state = 100;
                    }
                    break;

                case 2:
                    indice++;
                    if (indice >= words.length) {
                        this.report.add("state 2 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if (this.verbaliser.isDeterminer(word)){
                        state = 12;
                    }
                    else state = 11;
                    break;

                case 11:
                    word = "a "+word;
                    words[indice] = word;
                    String newFormatted = "";
                    for (int j = 0; j < words.length; j++) {
                        newFormatted+=words[j]+" ";
                    }
                    words = newFormatted.split(" ");

                    //for (int j = 0; j < words.length; j++) {
                    //    System.out.println("words "+ j +" = "+words[j]);
                    //}
                    // System.out.println("indice "+ indice);
                    state = 12;
                    break;

                case 12:
                    indice++;
                    if (indice >= words.length) {
                        this.report.add("state 12 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if (this.verbaliser.isNoun(word)) {

                        state = 13;
                    }
                    else if (this.verbaliser.isAdjectiveParticiple(word))
                        state = 121;

                    else {
                        state =100;
                        this.report.add("state 12 : next ['"+word+"'] is not a noun nor a adj/part");
                    }
                    break;

                case 13:
                    indice ++;
                    if (indice >= words.length) {
                        finished = true;
                        propertyType = PropertyType.OP_HAS_NOUNS;
                    }
                    else {
                        word = words[indice];

                        if (this.verbaliser.isPreposition(word))
                            state = 14;
                        else if (this.verbaliser.isNoun(word))
                            state = 13;
                        else {
                            state = 100;
                            this.report.add("state 13 : next ['"+word+"'] is not a noun nor a prep");
                        }
                    }
                    break;

                case 14:
                    indice ++;
                    if (indice >= words.length) {
                        this.report.add("state 14 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if (this.verbaliser.isPreposition(word))
                        state = 14;
                    else if (this.verbaliser.isNoun(word))
                        state = 13;
                    else {
                        state = 100;
                        this.report.add("state 14 : next ['"+word+"'] is not a prep nor a adj/part");
                    }
                    break;

                case 121:
                    indice ++;
                    if (indice >= words.length) {
                        this.report.add("state 121 : final state unreachable (no more words)");
                        state = 100;
                        continue;
                    }else word = words[indice];

                    if (this.verbaliser.isAdjectiveParticiple(word))
                        state = 121;
                    else if (this.verbaliser.isNoun(word))
                        state = 13;
                    else {
                        state = 100;
                        this.report.add("state 121 : next ['"+word+"'] is not a noun nor a adj/part");
                    }
                    break;

                case 3:
                    indice ++;
                    if (indice >= words.length) {
                        finished = true;
                        propertyType = PropertyType.OP_VERB;
                    }else{
                        word = words[indice];
                        if (this.verbaliser.isPreposition(word)) {
                            state = 15;
                        }
                        else {
                            state = 100;
                            this.report.add("state 3 : next ['"+word+"'] is not a prep");
                        }
                    }


                    break;
                case 15:
                    indice++;
                    if (indice >= words.length) {
                        finished = true;
                        propertyType =PropertyType.OP_VERB_PREP;
                    }
                    else {
                        state = 100;
                        this.report.add("state 15 : final state unreachable (no more words)");
                    }
                    break;
                case 100:
                    finished = true;

                    break;
                default:
                    break;
            }

            this.addStateToPath(state);
        }

        return propertyType;
    }

    public void printReprot() {
        System.out.println("\tReport "+this.getFormattedProperty());
        for(String r: this.report){
            System.out.println("\t\t"+r);
        }
    }
}
