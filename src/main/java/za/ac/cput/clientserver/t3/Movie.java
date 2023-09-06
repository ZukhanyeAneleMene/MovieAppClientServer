
package za.ac.cput.clientserver.t3;

import java.io.Serializable;

/**
 *
 * @author 219404275 Zukhanye Anele Mene
 */
public class Movie implements Serializable {
    
    private String movieTitle;
    private String movieDirector;
    private String movieGenre;
    
    
    public Movie() {
    }

    public Movie(String movieTitle, String movieDirector, String movieGenre) {
        this.movieTitle = movieTitle;
        this.movieDirector = movieDirector;
        this.movieGenre = movieGenre;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    @Override
    public String toString() {
        return "Movie{" + "movieTitle=" + movieTitle + ", movieDirector=" + movieDirector + ", movieGenre=" + movieGenre + '}';
    }

            
}
