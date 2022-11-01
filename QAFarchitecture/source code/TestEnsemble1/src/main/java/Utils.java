import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import java.util.ArrayList;
import java.util.Set;

public class Utils {

    public static ArrayList<String> SetOfClassesToStr(Set<OWLClass> owlClasses) {
        ArrayList<String> results = new ArrayList<>();

        for (OWLClass owlClass: owlClasses
        ) {
            if (!owlClass.toString().contains("owl:Nothing")) {
                String res = owlClass.toString();
                results.add(res);
            }
        }
        return cleanResults(results);
    }
    public static ArrayList<String> SetOfClassesToStrFormatted(Set<OWLClass> owlClasses) {
        ArrayList<String> results = new ArrayList<>();

        for (OWLClass owlClass: owlClasses
        ) {
            System.out.println(owlClass.toString());
            String res = getEntityName(owlClass.toString());
            results.add(":"+res);
        }
        return results;
    }

    public static ArrayList<String> SetOfOPsToStr(Set<OWLObjectPropertyExpression> owlOPs) {
        ArrayList<String> results = new ArrayList<>();
        for (OWLObjectPropertyExpression owlOP: owlOPs
        ) {
            if (!owlOP.getNamedProperty().getNamedProperty().toString().contains("owl:bottomObjectProperty")&& !owlOP.getNamedProperty().getNamedProperty().toString().contains("owl:topObjectProperty"))
                results.add(owlOP.getNamedProperty().toString());
        }
        return cleanResults(results);
    }
    public static ArrayList<String> cleanResults(ArrayList<String> inputs) {
        ArrayList<String> output = new ArrayList<>();
        for (String input: inputs
             ) {
            if (!output.contains(input)) {
                output.add(input);
            }
        }
        return output;
    }
    public static ArrayList<String> SetOfOPsToStrFormatted(Set<OWLObjectPropertyExpression> owlOPs) {
        ArrayList<String> results = new ArrayList<>();

        for (OWLObjectPropertyExpression owlOP: owlOPs
        ) {
            String res = getEntityName(owlOP.getNamedProperty().toString());
            results.add(""+res);
        }
        return results;
    }

    public static String getEntityName(String input)
    {
        String output = "";

        // return only chars of the '#'
        try {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                int x = input.indexOf('>');
                if (c == '#') {
                    output = input.substring(i+1, x);
                }
            }
        }
        catch (Exception e) {
        }
        if ("".equals(output)&&input.contains("/")) {
            if (input.equals("owl:Nothing"))
                return "";
            System.out.println("input: "+input);
            String[] arr = input.split("/");
            output = arr[arr.length-1];
            int x2 = output.indexOf('>');
            output = output.substring(0, x2);
        }
        return output;
    }
}
