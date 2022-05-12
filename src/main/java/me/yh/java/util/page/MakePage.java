package me.yh.java.util.page;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface MakePage {

    default <T> Page<T> makePage(Map<String, Object> params, Function<Map<String, Object>, List<T>> getList, Function<Map<String, Object>, Long> getCount) {
        int number = params.get("pageNumber") == null ? 1 : Integer.parseInt((String) params.get("pageNumber"));
        int size = params.get("pageSize") == null ? 0 : Integer.parseInt((String) params.get("pageSize"));
        if (size > 100) size = 1;

        Page<T> page;
        if (size == 0) page = new Page<>(number);
        else page = new Page<>(number, size);

        long totalElements;
        if (params.get("cachedCount") == null) {
            totalElements = getCount.apply(params);
        } else {
            totalElements = Long.parseLong(String.valueOf(params.get("cachedCount")));
        }
        page.setTotalElements(totalElements);

        if (totalElements == 0) {
            page.setContent(Collections.emptyList());
            return page;
        }

        params.put("startRowNum", page.getStartRowNum());
        params.put("endRowNum", page.getEndRowNum());

        params.put("limit", page.getLimit());
        params.put("offset", page.getOffset());

        page.setContent(getList.apply(params));

        //아래는 cachedCount를 사용할 경우 대비
        long realTotalElements = (long) (page.getNumber() - 1) * page.getSize() + page.getNumberOfElements();

        // 마지막 페이지가 아니면서 페이징 사이즈랑 다른경우(삭제된경우)
        // 마지막 페이지인데 총개수와 실제 개수가 다른 경우 (추가 또는 삭제)
        if ((!page.isLast() && page.getNumberOfElements() != page.getSize()) || (page.isLast() && realTotalElements != totalElements)) {
            totalElements = getCount.apply(params);
            page.setTotalElements(totalElements);
        }

        return page;
    }
}
