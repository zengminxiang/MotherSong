package com.fanstech.mothersong.main_fragment.community_publish.bean;

import java.io.Serializable;

/**
 *
 * @创始人：曾敏祥
 * @时间：下午 4:21 2016/4/13 0013
 * @功能模块：图片的信息
 */

public class ImageItem implements Serializable
{
    private static final long serialVersionUID = -7188270558443739436L;
    public String imageId;
    public String thumbnailPath;//缩略图的路径
    public String sourcePath;//源图片的路径
    public boolean isSelected = false;

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
