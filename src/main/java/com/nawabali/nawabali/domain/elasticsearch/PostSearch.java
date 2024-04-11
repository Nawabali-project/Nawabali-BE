package com.nawabali.nawabali.domain.elasticsearch;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "post")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Setting(settingPath = "elastic/es-setting.json")
@Mapping(mappingPath = "elastic/es-mapping.json")
public class PostSearch {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String contents;

    @Field(type = FieldType.Long)
    private Long postId;

}
