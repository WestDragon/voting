package by.it_academy.jd2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/vote")
public class VotingServlet extends HttpServlet {

    private final static String ARTIST_PARAM_NAME = "artist";
    private final static String GENRE_PARAM_NAME = "genre";
    private final static String ABOUT_PARAM_NAME = "about";

    private final Map<String, Integer> artistsScore = new HashMap<>();
    private final Map<String, Integer> genresScore = new HashMap<>();
    private final List<String> aboutList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String artist = req.getParameter(ARTIST_PARAM_NAME);
        String[] genres = req.getParameterValues(GENRE_PARAM_NAME);
        String about = req.getParameter(ABOUT_PARAM_NAME);

        artistsScore.compute(artist, (k, v) -> v != null ? v + 1 : 1);

        for (String genre : genres) {
            genresScore.compute(genre, (k, v) -> v != null ? v + 1 : 1);
        }

        aboutList.add(about);

        List<Map.Entry<String, Integer>> topArtist = getTopWithScore(artistsScore);
        List<Map.Entry<String, Integer>> topGenres = getTopWithScore(genresScore);

        PrintWriter writer = resp.getWriter();

        writeTo(topArtist, writer);
        writeBrakeLine(writer);
        writeTo(topGenres, writer);
        writeBrakeLine(writer);
        for (String line : aboutList) {
            writer.write(line + "</br>\n");
        }
    }

    private void writeBrakeLine(Writer writer) throws IOException {
        writer.write("***************</br>\n");
    }

    private void writeTo(List<Map.Entry<String, Integer>> data, Writer writer) throws IOException {
        for (Map.Entry<String, Integer> line : data) {
            writer.write(line.getKey() + ": " + line.getValue() + "</br>\n");
        }
    }

    private List<String> getTop(Map<String, Integer> data){
        return data.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<String, Integer>> getTopWithScore(Map<String, Integer> data){
        return data.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
    }
}
