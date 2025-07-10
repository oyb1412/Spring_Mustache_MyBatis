package kr.co.myproject.Util;

import lombok.Getter;

@Getter
public class PageInfo {
    private int id;
    private int displayId;

    public PageInfo(int id, int displayId)
    {
        this.id = id;
        this.displayId = displayId;
    }
}
