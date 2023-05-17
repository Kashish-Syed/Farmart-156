package com.fmt;

public class ListNode<T> {

	private ListNode<T> next; // this is for getting the next node
	private T value;

	public ListNode(T object) {
		this.value = object;
		this.next = null;
	}

	public ListNode<T> getNext() {
		return next;
	}
	

	public void setNext(ListNode<T> next) {
		this.next = next;
	}

	public T getValue() {
		return value;
	}

}
