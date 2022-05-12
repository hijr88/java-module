package me.yh.java.util.page;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({"startRowNum", "endRowNum", "offset", "limit"})
public class Page<T> {
    private int number;                 //현재 페이지 번호
    private int totalPages;             //총 페이지 번호
    private int size;                   //한 페이지에서 보여줄 리스트 개수

    private List<T> content;            //조회된 데이터
    private boolean hasContent;         //조회된 데이터 존재 여부
    private int numberOfElements;       //현재 페이지에 나온 데이터수

    private long totalElements = -1;    //총 개수

    private boolean isFirst;            //첫   페이지?
    private boolean isLast;             //마지막페이지?
    private boolean hasNext;            //다음 페이지가 있는지
    private boolean hasPrevious;        //이전 페이지가 있는지

    private final Map<String, Object> etc = new HashMap<>(); //기타 정보

    //RowNum DB
    private int startRowNum;            //시작 Row
    private int endRowNum;              //끝  Row

    //LIMIT ~ OFFSET DB
    private int offset;                 //시작 위치
    private int limit;                  //개수 제한

    public Page() {
        this(1,20);
    }

    public Page(int number) {
        this(number, 20);
    }

    public Page(int number, int size) {
        this.number = number;
        this.size = size;
        reSetting();
    }

    private void reSetting() {
        startRowNum = 1 + (number-1) * size;
        endRowNum = number * size;

        limit = size;
        offset = (number-1) * limit;

        if (totalElements == -1) return;

        totalPages = (int) Math.ceil(totalElements / (double) size);
        if (totalPages == 0) totalPages = 1;

        isFirst = number == 1;
        isLast = number == totalPages;

        hasNext = number < totalPages;
        hasPrevious = number > 1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        reSetting();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        reSetting();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
        hasContent = content.size() != 0;
        numberOfElements = content.size();
    }

    public boolean hasContent() {
        return hasContent;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        reSetting();
    }


    @JsonProperty("isFirst")
    public boolean isFirst() {
        return isFirst;
    }

    @JsonProperty("isLast")
    public boolean isLast() {
        return isLast;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public void addEtc(String key, Object value) {
        etc.put(key, value);
    }

    public int getStartRowNum() {
        return startRowNum;
    }

    public int getEndRowNum() {
        return endRowNum;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {

        return "Page{" +
                "number=" + number +
                (totalElements != -1 ?
                ", totalPages=" + totalPages : "") +

                ", size=" + size +

                (content != null ?
                ", content=" + content +
                ", hasContent=" + hasContent +
                ", numberOfElements=" + numberOfElements : "") +

                (totalElements != -1 ?
                ", totalElements=" + totalElements +
                ", isFirst=" + isFirst +
                ", isLast=" + isLast +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious : "") +

                ", etc=" + etc +

                ", startRowNum=" + startRowNum +
                ", endRowNum=" + endRowNum +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}