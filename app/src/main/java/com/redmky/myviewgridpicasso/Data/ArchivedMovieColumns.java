package com.redmky.myviewgridpicasso.Data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
/**
 * Created by redmky on 11/2/2015.
 */
public interface ArchivedMovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID =
            "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    String NAME =
            "name";
    @DataType(DataType.Type.INTEGER) @NotNull
    String VOTES =
            "dist_from_sun";
    @DataType(DataType.Type.TEXT) @NotNull
    String IMAGE_RESOURCE =
            "image_resource";
    @DataType(DataType.Type.TEXT) @NotNull
    String SYNOPSYS =
            "synopsys";
    @DataType(DataType.Type.TEXT) @NotNull
    String RELEASE_DATE =
            "release_date";
    @DataType(DataType.Type.TEXT) @NotNull
    String MOVIE_ID =
            "movie_id";



}
