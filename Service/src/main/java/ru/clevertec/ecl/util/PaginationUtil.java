package ru.clevertec.ecl.util;

public class PaginationUtil {
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer DEFAULT_PAGE = 0;

    public static Integer calculateOffset(Integer pageSize, Integer page){
        return pageSize*(page - 1);
    }
    public static Integer checkPageSize(Integer pageSize){
        if (pageSize == null || pageSize < 1){
            return DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static Integer checkPage(Integer page){
        if (page == null || page < 0){
            return DEFAULT_PAGE;
        }
        return page;
    }
}
