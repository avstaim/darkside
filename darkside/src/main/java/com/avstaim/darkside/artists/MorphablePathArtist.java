package com.avstaim.darkside.artists;

import androidx.annotation.NonNull;

/**
 * Advanced {@link PathArtist}, with ability to morph it's path.
 */
public class MorphablePathArtist extends PathArtist {
    public void setPathData(@NonNull PathParser.PathDataNode[] pathData) {
        mPath.reset();
        PathParser.PathDataNode.nodesToPath(pathData, mPath);

        initBounds();
        mIsPathDirty = true;
    }
}
