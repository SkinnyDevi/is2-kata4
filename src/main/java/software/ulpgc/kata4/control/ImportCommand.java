package software.ulpgc.kata4.control;

import software.ulpgc.kata4.FileImportable;
import software.ulpgc.kata4.io.*;
import software.ulpgc.kata4.model.MovieTitle;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImportCommand implements Command {
    private final FileImportable importer;

    public ImportCommand(FileImportable importer) {
        this.importer = importer;
    }

    @Override
    public void execute() {
        try (MovieTitleReader reader = new GzipMovieTitleReader(importedFile(), tsvMovieDeserializer()); MovieTitleWriter writer = SqlLiteDBMovieTitleWriter.open(outputFile())) {
            executeCommand(reader, writer);
            System.out.println("Finished writing to DB.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void executeCommand(MovieTitleReader reader, MovieTitleWriter writer) throws IOException {
        Optional<MovieTitle> movie = reader.read();
        while (movie.isPresent()) {
            writer.write(movie.get());
            movie = reader.read();
        }
    }

    private File outputFile() {
        return new File("movies.db");
    }

    private MovieTitleDeserializer tsvMovieDeserializer() {
        return new TsvMovieTitleDeserializer();
    }

    private File importedFile() {
        return importer.get();
    }
}