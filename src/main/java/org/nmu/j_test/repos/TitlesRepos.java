package org.nmu.j_test.repos;

import org.nmu.j_test.models.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitlesRepos extends JpaRepository<Title,Integer> {

    List<Title> findAllByNameContainsIgnoreCase(String name);
}
