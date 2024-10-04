package edu.miu.cs.waa.lab5.repository;

import edu.miu.cs.waa.lab5.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

}
