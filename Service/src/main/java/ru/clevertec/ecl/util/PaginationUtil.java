package ru.clevertec.ecl.util;

public class PaginationUtil {
    public static Integer calculateOffset(Integer pageSize, Integer page){
        return pageSize*(page - 1);
    }
}
