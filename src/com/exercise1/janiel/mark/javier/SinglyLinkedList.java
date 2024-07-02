package com.exercise1.janiel.mark.javier;
/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * A basic singly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class SinglyLinkedList<E> implements Cloneable {
  //---------------- nested Node class ----------------
  /**
   * Node of a singly linked list, which stores a reference to its
   * element and to the subsequent node in the list (or null if this
   * is the last node).
   */
  private static class Node<E> {

    /** The element stored at this node */
    private E element;            // reference to the element stored at this node

    /** A reference to the subsequent node in the list */
    private Node<E> next;         // reference to the subsequent node in the list

    /**
     * Creates a node with the given element and next node.
     *
     * @param e  the element to be stored
     * @param n  reference to a node that should follow the new node
     */
    public Node(E e, Node<E> n) {
      element = e;
      next = n;
    }

    // Accessor methods
    /**
     * Returns the element stored at the node.
     * @return the element stored at the node
     */
    public E getElement() { return element; }

    /**
     * Returns the node that follows this one (or null if no such node).
     * @return the following node
     */
    public Node<E> getNext() { return next; }

    // Modifier methods
    /**
     * Sets the node's next reference to point to Node n.
     * @param n    the node that should follow this one
     */
    public void setNext(Node<E> n) { next = n; }
    
  } //----------- end of nested Node class -----------

  // instance variables of the SinglyLinkedList
  /** The head node of the list */
  private Node<E> head = null;               // head node of the list (or null if empty)

  /** The last node of the list */
  private Node<E> tail = null;               // last node of the list (or null if empty)

  /** Number of nodes in the list */
  private int size = 0;                      // number of nodes in the list

  /** Constructs an initially empty list. */
  public SinglyLinkedList() { }              // constructs an initially empty list

  // access methods
  /**
   * Returns the number of elements in the linked list.
   * @return number of elements in the linked list
   */
  public int size() { return size; }

  /**
   * Tests whether the linked list is empty.
   * @return true if the linked list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the first element of the list
   * @return element at the front of the list (or null if empty)
   */
  public E first() {             // returns (but does not remove) the first element
    if (isEmpty()) return null;
    return head.getElement();
  }

  /**
   * Returns (but does not remove) the last element of the list.
   * @return element at the end of the list (or null if empty)
   */
  public E last() {              // returns (but does not remove) the last element
    if (isEmpty()) return null;
    return tail.getElement();
  }

  // update methods
  /**
   * Adds an element to the front of the list.
   * @param e  the new element to add
   */
  public void addFirst(E e) {                // adds element e to the front of the list
    head = new Node<>(e, head);              // create and link a new node
    if (size == 0)
      tail = head;                           // special case: new node becomes tail also
    size++;
  }

  /**
   * Adds an element to the end of the list.
   * @param e  the new element to add
   */
  public void addLast(E e) {                 // adds element e to the end of the list
    Node<E> newest = new Node<>(e, null);    // node will eventually be the tail
    if (isEmpty())
      head = newest;                         // special case: previously empty list
    else
      tail.setNext(newest);                  // new node after existing tail
    tail = newest;                           // new node becomes the tail
    size++;
  }

  /**
   * Removes and returns the first element of the list.
   * @return the removed element (or null if empty)
   */
  public E removeFirst() {                   // removes and returns the first element
    if (isEmpty()) return null;              // nothing to remove
    E answer = head.getElement();
    head = head.getNext();                   // will become null if list had only one node
    size--;
    if (size == 0)
      tail = null;                           // special case as list is now empty
    return answer;
  }

  @SuppressWarnings({"unchecked"})
  public boolean equals(Object o) {
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    SinglyLinkedList other = (SinglyLinkedList) o;   // use nonparameterized type
    if (size != other.size) return false;
    Node walkA = head;                               // traverse the primary list
    Node walkB = other.head;                         // traverse the secondary list
    while (walkA != null) {
      if (!walkA.getElement().equals(walkB.getElement())) return false; //mismatch
      walkA = walkA.getNext();
      walkB = walkB.getNext();
    }
    return true;   // if we reach this, everything matched successfully
  }

  @SuppressWarnings({"unchecked"})
  public SinglyLinkedList<E> clone() throws CloneNotSupportedException {
    // always use inherited Object.clone() to create the initial copy
    SinglyLinkedList<E> other = (SinglyLinkedList<E>) super.clone(); // safe cast
    if (size > 0) {                    // we need independent chain of nodes
      other.head = new Node<>(head.getElement(), null);
      Node<E> walk = head.getNext();      // walk through remainder of original list
      Node<E> otherTail = other.head;     // remember most recently created node
      while (walk != null) {              // make a new node storing same element
        Node<E> newest = new Node<>(walk.getElement(), null);
        otherTail.setNext(newest);     // link previous node to this one
        otherTail = newest;
        walk = walk.getNext();
      }
    }
    return other;
  }

  public int hashCode() {
    int h = 0;
    for (Node walk=head; walk != null; walk = walk.getNext()) {
      h ^= walk.getElement().hashCode();      // bitwise exclusive-or with element's code
      h = (h << 5) | (h >>> 27);              // 5-bit cyclic shift of composite code
    }
    return h;
  }

  /**
   * Produces a string representation of the contents of the list.
   * This exists for debugging purposes only.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    Node<E> walk = head;
    while (walk != null) {
      sb.append(walk.getElement());
      if (walk != tail)
        sb.append(", ");
      walk = walk.getNext();
    }
    sb.append(")");
    return sb.toString();
  }
  
  //method to swap two nodes
  public void swapTwoNodes(Node<E> node1, Node<E> node2) {
	  
	    //Case when either list is empty
	    if (head == null) {
	        return; //nothing to swap
	    }
	    
	    if (node1 == node2) //don't swap and just return value if nodes are the same
	    	return; 

	    //Initialize previous nodes for node1 and node2. This previous nodes are needed to track nodes before node1/2 
	    // because upon swapping, we need to update the next pointers of these previous nodes and point them to node1/2.
	    Node<E> prevNode1 = null; 
	    Node<E> prevNode2 = null; 
	    Node<E> walk = head; //walk from the head of the list
	    
	    
	    ///Traverse the list to find the previous nodes of node1 and node2
	    while (walk != null) {
	      if (walk.getNext() == node1) prevNode1 = walk;
	      if (walk.getNext() == node2) prevNode2 = walk;
	      walk = walk.getNext();
	    }

	    // If prevNode1 is not null, set its next node to node2
	    if (prevNode1 != null) prevNode1.setNext(node2);
	    else head = node2;   	    //Otherwise, set the head of the list to node2

	    // If prevNode2 is not null, set its next node to node1
	    if (prevNode2 != null) prevNode2.setNext(node1);
	    else head = node1;   //Otherwise, set the head of the list to node1
	    
	    //Swapping the next nodes of node1 and node2. First, store node1’s next node in a temporary variable. Then set node1’s next node to node2’s next node
	    //Then set node2’s next node to the node stored in the temporary node var.
	    Node<E> tempNode = node1.getNext();
	    node1.setNext(node2.getNext());
	    node2.setNext(tempNode);
	    
	    //update the tail pointer
	    if (tail == node1)
	    	tail = node2;    //If the tail was pointing to node1, set it to point to node2 (swap)
	    else if 
	    	(tail == node2)
	    	tail = node1;  //If the tail was pointing to node2, set it to point to node1 (swap)
	  }
  
  
  //main method
  //Student Number: 301379377
  //Student Name: Janiel Mark Javier
  public static void main(String[] args)
  {
	  
	  SinglyLinkedList<String> list = new SinglyLinkedList<String>();
	  list.addFirst("MSP");
	  list.addLast("ATL");
	  list.addLast("BOS");
	  list.addFirst("LAX");
	  System.out.println("List BEFORE swapping nodes");
	  System.out.println(list);
	  
	  //TEST SCENARIOS using references of nodes and not specific contents
	    //Swap 2nd and 4th
//	    Node<String> node1 = list.head.getNext(); // "MSP"
//	    Node<String> node2 = list.tail; // "BOS"
	   
		//Swap 2nd and 3rd
//	    Node<String> node1 = list.head.getNext(); // "MSP"
//	    Node<String> node2 = list.head.getNext().getNext(); // "ATL"
	  
		//Swap 3rd and 4th
//	    Node<String> node1 = list.head.getNext().getNext(); // "ATL"
//	    Node<String> node2 = list.tail; // "BOS"
	    
		//Swap 1st and 3rd
//	    Node<String> node1 = list.head; // "LAX"
//	    Node<String> node2 = list.head.getNext().getNext(); // "ATL"
	  
		//Swap 1st and 2nd
//	    Node<String> node1 = list.head; // "LAX"
//	    Node<String> node2 = list.head.getNext(); // "MSP"
	  
		//Swap same node
//	    Node<String> node1 = list.head; // "LAX"
//	    Node<String> node2 = list.head; // "LAX"
	    
		//Swap 1st and 4th
	    Node<String> node1 = list.head; // "LAX"
	    Node<String> node2 = list.tail; // "BOS"
	    	    
	   list.swapTwoNodes(node1, node2);
	    System.out.println("\nList AFTER swapping nodes");
	    System.out.println(list);
  }
  
}
