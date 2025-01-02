package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

import java.io.IOException;

public interface MovieTitleWriter extends AutoCloseable {
    void write(MovieTitle movie) throws IOException;
}