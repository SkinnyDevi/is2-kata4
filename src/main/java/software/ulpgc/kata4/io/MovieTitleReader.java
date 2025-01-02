package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.io.IOException;
import java.util.Optional;

public interface MovieTitleReader extends AutoCloseable {
    Optional<MovieTitle> read() throws IOException;
}