package com.example.javaspringlearn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {
    private double sliceLength;
    private double stopInd;
    private double startInd;
    private Page[] pages;

    public Pagination(double sliceLength, double stopInd, double startInd, Page[] pages) {
        this.sliceLength = sliceLength;
        this.stopInd = stopInd;
        this.startInd = startInd;
        this.pages = pages;
    }
}
