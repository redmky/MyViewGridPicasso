package com.redmky.myviewgridpicasso.Data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by redmky on 11/6/2015.
 */
public interface ArchivedChildColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID =
            "_id";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String MOVIE_ID =
            "movieId";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String review =
            "review";
}