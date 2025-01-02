package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.io.*;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class GzipMovieTitleReader implements MovieTitleReader{
    private final BufferedReader reader;
    private final MovieTitleDeserializer deserializer;

    public GzipMovieTitleReader(File file, MovieTitleDeserializer deserializer) throws IOException {
        this.deserializer = deserializer;
        this.reader = readerOf(file);
        skipHeader();
    }

    private BufferedReader readerOf(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(gzipInputStream(file)));
    }

    private InputStream gzipInputStream(File file) throws IOException {
        return new GZIPInputStream(new FileInputStream(file));
    }

    private void skipHeader() throws IOException {
        this.reader.readLine();
    }

    @Override
    public Optional<MovieTitle> read() throws IOException {
        return deserialize(reader.readLine());
    }

    private Optional<MovieTitle> deserialize(String line) {
        if (line != null) return Optional.of(deserializer.deserialize(line));
        return Optional.empty();
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}