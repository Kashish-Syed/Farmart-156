package com.fmt;

import java.util.Comparator;
import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {

	private ListNode<T> head = null;
	private int size = 0;
	private Comparator<T> cmp;

	/**
	 * comparator
	 * 
	 * @param cmp
	 */
	public MyLinkedList(Comparator<T> cmp) {
		this.cmp = cmp;
	}

	public int getSize() {
		return this.size;
	}

	private ListNode<T> getHead() {
		return head;
	}

	public Comparator<T> getCmp() {
		return cmp;
	}

	/**
	 * This function clears out the contents of the list, making it an empty list.
	 */
	public void clear() {
		if (size == 0) {
			this.head = null;
		} else if (size != 0) {
			for (int i = 0; i < this.size; i++) {
				remove(i);
				size--;
			}
		}
	}

	/**
	 * Private method to remove an element from the provided position
	 * 
	 * @param position
	 */
	private void remove(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + position);
		} else if (position == 0) {
			// remove the head
			this.head = this.head.getNext();
			this.size--;
		} else {
			ListNode<T> previous = this.getListNode(position - 1);
			ListNode<T> current = previous.getNext();
			previous.setNext(current);
			this.size--;
		}
	}

	/**
	 * This is a private helper method that returns a {@link ListNode} corresponding
	 * to the given position.
	 * 
	 * @param position
	 * @return
	 */
	private ListNode<T> getListNode(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + position);
		}
		ListNode<T> current = this.head;
		for (int i = 0; i < position; i++) {
			current = current.getNext();
		}

		return current;
	}

	/**
	 * Returns the {@link T} element stored at the given <code>position</code>.
	 * 
	 * @param position
	 * @return
	 */
	public T getValue(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + position);
		}
		return this.getListNode(position).getValue();
	}

	/**
	 * The method to add the invoice by using the comparator
	 * 
	 * @param element
	 * @return
	 */
	public void add(T element) {
		if (head == null) {
			head = new ListNode<T>(element);

		} else {
			ListNode<T> newNode = new ListNode<T>(element);
			ListNode<T> current = head;
			ListNode<T> previous = null;
			while (current != null && cmp.compare(current.getValue(), element) < 0) {

				// iterate through the list
				previous = current;
				current = current.getNext();

			}
			if (previous == null) {
				head = newNode;
			} else {
				previous.setNext(newNode);

			}
			newNode.setNext(current);

		}
		size++;
	}

	/** 
	 * Iterator that iterates over the linked list.
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private ListNode<T> current = head;

			@Override
			public boolean hasNext() {
				return (current != null);
			}

			@Override
			public T next() {
				T result = null;
				if (hasNext()) {
					this.current = current.getNext();
				}
				return result;
			}

		};
	}
}
