package utils;

public enum AnsiColor {

    // color table: https://upload.wikimedia.org/wikipedia/commons/3/34/ANSI_sample_program_output.png

    RED("\u001B[31m", "red"),
    GREEN("\u001B[32m", "green"),
    YELLOW("\u001B[33m", "yellow"),
    BLUE("\u001B[34m", "blue"),
    PURPLE("\u001B[35m", "purple"),
    CYAN("\u001B[36m", "cyan"),
    RESET("\u001B[0m", "reset");

    private final String colorValue;
    private final String colorName;

    AnsiColor(String colorValue, String colorName) {
        this.colorValue = colorValue;
        this.colorName = colorName;
    }

    public String getColorValue() {
        return colorValue;
    }

    public String getColorName() {
        return colorName;
    }
}
