public class FormattedValue {
    private String formattedValue;
    private int removeString;

    public FormattedValue() {
        this.setRemoveString(0);
        this.setFormattedValue("");
    }
    public String getFormattedValue() {
        return formattedValue;
    }

    public void setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
    }

    public int getRemoveString() {
        return removeString;
    }

    public void setRemoveString(int removeString) {
        this.removeString = removeString;
    }
}
