package com.redmky.myviewgridpicasso.Data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by redmky on 11/1/2015.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private MovieDatabase(){}
    public static final int VERSION = 1;

        @Table(ArchivedMovieColumns.class) public static final String ARCHIVED_MOVIES =
            "archived_movies";



}
