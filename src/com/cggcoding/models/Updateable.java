package com.cggcoding.models;

/**
 * Created by cgrid_000 on 8/21/2015.
 */
//TODO delete this interface if it doesn't actually get used - especially if using DatabaseModel, which has a similar method
public interface Updateable {
    boolean updateData(Task taskWithNewData);
}
