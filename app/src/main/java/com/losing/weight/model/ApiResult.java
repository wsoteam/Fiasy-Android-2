package com.losing.weight.model;

import java.util.List;

public class ApiResult<T> {

  private int count;
  private Object next;
  private Object previous;
  private List<T> results;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Object getNext() {
    return next;
  }

  public void setNext(Object next) {
    this.next = next;
  }

  public Object getPrevious() {
    return previous;
  }

  public void setPrevious(Object previous) {
    this.previous = previous;
  }

  public List<T> getResults() {
    return results;
  }

  public void setResults(List<T> results) {
    this.results = results;
  }
}
