package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.MovieTitle;

public interface MovieTitleDeserializer {
    MovieTitle deserialize(String text);
}