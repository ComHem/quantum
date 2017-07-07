package se.comhem.quantum.integration.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<PostDb, String> {

    List<PostDb> findByUpdateDateAfter(LocalDateTime localDateTime);
}
