package org.nmu.j_test.services;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.nmu.j_test.models.Title;
import org.nmu.j_test.repos.TitlesRepos;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ALSiteParserService {

    final static String baseUrl = "https://www.anilibria.tv";

    final TitlesRepos repos;
    final TimeService timeService;

    public ALSiteParserService(TitlesRepos repos, TimeService timeService) {
        this.repos = repos;
        this.timeService = timeService;
    }

    @SneakyThrows
    public List<Title> findTitles(String search) {

        var titlesToSave = new ArrayList<Title>();

        var titleSearchResult = new ArrayList<>(repos.findAllByNameContainsIgnoreCase(search));

        var alphpbDocument = Jsoup.connect(baseUrl + "/pages/alphabet.php").get();

        Date dateNow = timeService.getDateNow();

        for (Element titleElement : alphpbDocument.getElementsByClass("goodcell")) {

            String title = Objects.requireNonNull(
                    titleElement.getElementsByClass("schedule-runame").first()).text();

            if (titleSearchResult.stream().anyMatch(p -> Objects.equals(p.getName(), title))) continue;

            if (!title.toLowerCase().contains(search.toLowerCase())) continue;

            String link = baseUrl + Objects.requireNonNull(
                            titleElement.getElementsByTag("a").first()).attr("href");

            var titleDocument = Jsoup.connect(link).get();

            String description = Objects.requireNonNull(
                    titleDocument.getElementsByClass("detail-description").first()).text();

            String season = Objects.requireNonNull(
                    titleDocument.getElementsByClass("release-season").first()).text();

            var titleResult = Title.builder()
                    .name(title)
                    .description(description)
                    .season(season)
                    .link(link)
                    .lastUpdate(dateNow)
                    .build();
            titleSearchResult.add(titleResult);
            titlesToSave.add(titleResult);
        }

        repos.saveAll(titlesToSave);
        return titleSearchResult;
    }

}
