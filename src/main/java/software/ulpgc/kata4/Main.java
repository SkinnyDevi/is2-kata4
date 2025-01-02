package software.ulpgc.kata4;

import software.ulpgc.kata4.control.ImportCommand;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        new ImportCommand(createImportDialog()).execute();
    }

    private static FileImportable createImportDialog() {
        return () -> new File("title.basics.tsv.gz");
    }
}