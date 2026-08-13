package ua.reactive.mongo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "language")
public class Language {

    @Id
    @JsonIgnore
    private String id;
    private String name;
    private String creator;
    private String feature;
}
