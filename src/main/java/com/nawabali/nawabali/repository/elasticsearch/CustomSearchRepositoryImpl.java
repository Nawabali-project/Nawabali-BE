//package com.nawabali.nawabali.repository.elasticsearch;
//
//import com.nawabali.nawabali.domain.elasticsearch.PostSearch;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.Query;
//import org.springframework.data.elasticsearch.core.query.StringQuery;
//import org.springframework.stereotype.Component;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Component
//public class CustomSearchRepositoryImpl implements CustomSearchRepository {
//
//    private final ElasticsearchOperations elasticsearchOperations;
//
//
//    @Override
//    public List<PostSearch> findByContentsContaining(String contents) {
//        Query query = new StringQuery("{\"match\": {\"contents\": \"" + contents + "\"}}");
//        SearchHits<PostSearch> searchHits = elasticsearchOperations.search(query, PostSearch.class);
//
//        return searchHits.getSearchHits().stream()
//                .map(SearchHit::getContent)
//                .collect(Collectors.toList());
//    }
//
//}
