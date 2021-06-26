package rbt;

import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class RedBlackTree {

	static People[] People = new People[52];// to fold people.txt file data
	static Date[] date = new Date[52];// to add people.birtdate, because people.birtdate variable type is date
	Node[] node = new Node[52];// to insert to red black tree all the data in people array
	static int allPeopleNumber = 0;// to GETNUM function, hold the all people in the rb-tree
	static int findSmallerNumber = 0;// to GETNUMSMALLER1 and GETNUMSMALLER2 functions, hold the number of smaller
										// nodes.
	int[] findDate = new int[3]; // to GETNUMSMALLER2 function, first function find input id in tree nodes and
									// then get birtdate data.

	private final int RED = 0;
	private final int BLACK = 1;

	private class Node {

		int key = -1, color = BLACK;
		int id;
		Date birtdate;
		Node left = nil, right = nil, parent = nil;

		public Node(int key) {
			this.key = key;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Date getBirtdate() {
			return birtdate;
		}

		public void setBirtdate(Date birtdate) {
			this.birtdate = birtdate;
		}

	}

	private final Node nil = new Node(-1);
	private Node root = nil;

	public void printTree(Node node) {
		if (node == nil) {
			return;
		}
		printTree(node.left);
		System.out.print(((node.color == RED) ? "Color: Red " : "Color: Black ") + "Key: " + node.key + " Parent: "
				+ node.parent.key + "\n");
		printTree(node.right);
	}

	private Node findNode(Node findNode, Node node) {
		if (root == nil) {
			return null;
		}

		if (findNode.key < node.key) {
			if (node.left != nil) {
				return findNode(findNode, node.left);
			}
		} else if (findNode.key > node.key) {
			if (node.right != nil) {
				return findNode(findNode, node.right);
			}
		} else if (findNode.key == node.key) {
			return node;
		}
		return null;
	}

	private void insert(Node node) {
		Node temp = root;
		if (root == nil) {
			root = node;
			node.color = BLACK;
			node.parent = nil;
		} else {
			node.color = RED;
			while (true) {
				if (node.key < temp.key) {
					if (temp.left == nil) {
						temp.left = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.left;
					}
				} else if (node.key >= temp.key) {
					if (temp.right == nil) {
						temp.right = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.right;
					}
				}
			}
			fixTree(node);
		}
	}

	// Takes as argument the newly inserted node
	private void fixTree(Node node) {
		while (node.parent.color == RED) {
			Node uncle = nil;
			if (node.parent == node.parent.parent.left) {
				uncle = node.parent.parent.right;

				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.right) {
					// Double rotation needed
					node = node.parent;
					rotateLeft(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateRight(node.parent.parent);
			} else {
				uncle = node.parent.parent.left;
				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.left) {
					// Double rotation needed
					node = node.parent;
					rotateRight(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateLeft(node.parent.parent);
			}
		}
		root.color = BLACK;
	}

	void rotateLeft(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.right;
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent;
			node.parent = node.right;
			if (node.right.left != nil) {
				node.right.left.parent = node;
			}
			node.right = node.right.left;
			node.parent.left = node;
		} else {// Need to rotate root
			Node right = root.right;
			root.right = right.left;
			right.left.parent = root;
			root.parent = right;
			right.left = root;
			right.parent = nil;
			root = right;
		}
	}

	void rotateRight(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			node.left.parent = node.parent;
			node.parent = node.left;
			if (node.left.right != nil) {
				node.left.right.parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {// Need to rotate root
			Node left = root.left;
			root.left = root.left.right;
			left.right.parent = root;
			root.parent = left;
			left.right = root;
			left.parent = nil;
			root = left;
		}
	}

	// Deletes whole tree
	void deleteTree() {
		root = nil;
	}

	// Deletion Code .

	// This operation doesn't care about the new Node's connections
	// with previous node's left and right. The caller has to take care
	// of that.
	void transplant(Node target, Node with) {
		if (target.parent == nil) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	boolean delete(Node z) {
		if ((z = findNode(z, root)) == null)
			return false;
		Node x;
		Node y = z; // temporary reference y
		int y_original_color = y.color;

		if (z.left == nil) {
			x = z.right;
			transplant(z, z.right);
		} else if (z.right == nil) {
			x = z.left;
			transplant(z, z.left);
		} else {
			y = treeMinimum(z.right);
			y_original_color = y.color;
			x = y.right;
			if (y.parent == z)
				x.parent = y;
			else {
				transplant(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}
			transplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		if (y_original_color == BLACK)
			deleteFixup(x);
		return true;
	}

	void deleteFixup(Node x) {
		while (x != root && x.color == BLACK) {
			if (x == x.parent.left) {
				Node w = x.parent.right;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateLeft(x.parent);
					w = x.parent.right;
				}
				if (w.left.color == BLACK && w.right.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.right.color == BLACK) {
					w.left.color = BLACK;
					w.color = RED;
					rotateRight(w);
					w = x.parent.right;
				}
				if (w.right.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.right.color = BLACK;
					rotateLeft(x.parent);
					x = root;
				}
			} else {
				Node w = x.parent.left;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateRight(x.parent);
					w = x.parent.left;
				}
				if (w.right.color == BLACK && w.left.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.left.color == BLACK) {
					w.right.color = BLACK;
					w.color = RED;
					rotateLeft(w);
					w = x.parent.left;
				}
				if (w.left.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.left.color = BLACK;
					rotateRight(x.parent);
					x = root;
				}
			}
		}
		x.color = BLACK;
	}

	Node treeMinimum(Node subTreeRoot) {
		while (subTreeRoot.left != nil) {
			subTreeRoot = subTreeRoot.left;
		}
		return subTreeRoot;
	}

	public void consoleUI() throws IOException {
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.println("\n1.- INSERT A NEW PERSON\n" + "2.- GETNUMSMALLER1\n" + "3.- GETNUMSMALLER2\n"
					+ "4.- GETMAX\n" + "5.- GETMIN\n" + "6.- GETNUM\n" + "7.- PRINT");
			int choice = scan.nextInt();

			int item;
			RedBlackTree rbt = new RedBlackTree();
			switch (choice) {
			case 1:
				readPeople();// read all data
				rbt.Insert(rbt);
				System.out.println("all item were inserted.");
				break;
			case 2:
				rbt.Insert(rbt);
				rbt.GETNUMSMALLER1(rbt.root);
				findSmallerNumber = 0; // when GETNUMSMALLER1 function over, I have to assign zero to variable, because
										// user may call same function again
				break;
			case 3:
				rbt.Insert(rbt);
				rbt.GETNUMSMALLER2(rbt.root);
				findSmallerNumber = 0;// same thing above...
				break;
			case 4:
				rbt.Insert(rbt);
				rbt.GetMax(rbt);
				break;
			case 5:
				rbt.Insert(rbt);
				rbt.GetMin(rbt);
				break;
			case 6:
				rbt.Insert(rbt);
				System.out.println("The number of all people is " + rbt.GetNum(rbt.root));
				allPeopleNumber = 0;// same thing above...
				break;
			case 7:
				readPeople();
				rbt.Insert(rbt);
				rbt.Print(rbt.root);
				break;
			}
		}
	}

	static void readPeople() throws IOException {

		FileInputStream fileRead = new FileInputStream("people.txt");
		DataInputStream data = new DataInputStream(fileRead);
		BufferedReader read = new BufferedReader(new InputStreamReader(data));
		int numOfPeople = 0;// this is like "i" in for loop, for people array assign without for loop

		String line;
		int id = 0, day = 0, month = 0, year = 0;
		String[] inputs = null;
		String[] date_inputs = null;

		while ((line = read.readLine()) != null) {
			// Every line of the file was assigned to 'line' at every turn.
			inputs = line.split(",");// split id and date

			for (int i = 0; i < inputs.length; i++) {
				switch (i) {
				case 0:
					id = Integer.parseInt(inputs[i]);
					break;
				case 1:
					date_inputs = inputs[i].split("/");// to split day month year
					for (int j = 0; j < date_inputs.length; j++) {
						switch (j) {
						case 0:
							day = Integer.parseInt(date_inputs[j]);
							break;
						case 1:
							month = Integer.parseInt(date_inputs[j]);
							break;
						case 2:
							year = Integer.parseInt(date_inputs[j]);
							break;
						}

					}
					break;
				}

			}
			date[numOfPeople] = new Date(day, month, year);
			People[numOfPeople] = new People(id, date[numOfPeople], 2018 - year);
			numOfPeople++;

		}
		read.close();
	}

	public void Insert(RedBlackTree rbt) throws IOException {

		// insert a tree to node array to rb-tree
		for (int i = 0; i < People.length; i++) {
			node[i] = new Node(People[i].getAge());
			node[i].setKey(People[i].getAge());
			node[i].setBirtdate(People[i].getBirtdate());
			node[i].setId(People[i].getId());
			rbt.insert(node[i]);
		}

	}

	public void GetMin(RedBlackTree rbt) {
		Node temp = rbt.root;
		int min;
		int id;
		Date birtdate;
		do {
			min = temp.getKey();
			id = temp.getId();
			birtdate = temp.getBirtdate();
			temp = temp.left;
		} while (temp.left != null);// going to most left and find smallest node in tree

		System.out.print("The minumum age of all people is " + min + "  id: " + id + "  Birtdate: " + birtdate.getDay()
				+ "/" + birtdate.getMounth() + "/" + birtdate.getYear());
		System.out.println();

	}

	public void GetMax(RedBlackTree rbt) {
		Node temp = rbt.root;
		int max;
		int id;
		Date birtdate;
		do {
			max = temp.getKey();
			id = temp.getId();
			birtdate = temp.getBirtdate();
			temp = temp.right;
		} while (temp.right != null);// going to most right and find biggest node in tree

		System.out.print("The maximum age of all people is " + max + "  id: " + id + "  Birtdate: " + birtdate.getDay()
				+ "/" + birtdate.getMounth() + "/" + birtdate.getYear());
		System.out.println();

	}

	public void Print(Node node) {
		Node temp = node;
		if (temp.left != null) { // print all node with inorder traversal
			Print(temp.left);
			System.out.print(
					((node.color == RED) ? "Color: Red " : "Color: Black ") + " Key:" + temp.getKey() + " Parent: "
							+ temp.parent.getKey() + "  -   " + temp.getId() + "  -  " + temp.getBirtdate().getDay()
							+ "/" + temp.getBirtdate().getMounth() + "/" + temp.getBirtdate().getYear());
			System.out.println();
			Print(temp.right);
		}
	}

	public int GetNum(Node node) { // find all people number in rb-tree with inorder traversal
		Node temp = node;
		if (temp.left != null) {
			GetNum(temp.left);
			allPeopleNumber++;
			GetNum(temp.right);
		}
		return allPeopleNumber;

	}

	public int GetNum1(Node node, int day, int month, int year) { // to find smaller nodes number which given input
																	// birtdate data, this is recursive function for
																	// getnumsmaller1
		Node temp = node;
		if (temp.left != null) {
			GetNum1(temp.left, day, month, year);
			if (temp.getBirtdate().getYear() > year)
				findSmallerNumber++;
			else if (temp.getBirtdate().getYear() == year) {
				if (temp.getBirtdate().getMounth() > month)
					findSmallerNumber++;
				else if (temp.getBirtdate().getMounth() == month)
					if (temp.getBirtdate().getDay() > day)
						findSmallerNumber++;
			}
			GetNum1(temp.right, day, month, year);
		}
		return findSmallerNumber;

	}

	public void GETNUMSMALLER1(Node node) {
		Node temp = node;
		Scanner input = new Scanner(System.in);
		String[] date = null;
		int day = 0, month = 0, year = 0;
		System.out.print("Please enter a date: ");
		date = input.nextLine().split("/");
		day = Integer.parseInt(date[0]);
		month = Integer.parseInt(date[1]);
		year = Integer.parseInt(date[2]);

		System.out.println(GetNum1(temp, day, month, year));

	}

	public void findId(Node node, int id) {
		// this function find birtdate data for given id node, this for getnumsmaller2,
		// but it's just find birtdate.
		Node temp = node;
		if (temp.left != null) {
			findId(temp.left, id);
			if (temp.getId() == id) {
				findDate[0] = temp.getBirtdate().getDay();
				findDate[1] = temp.getBirtdate().getMounth();
				findDate[2] = temp.getBirtdate().getYear();
			}
			findId(temp.right, id);
		}
	}

	public void GETNUMSMALLER2(Node node) {
		Node temp = node;
		Scanner input = new Scanner(System.in);
		int id;
		System.out.print("Please enter a id: ");
		id = input.nextInt();
		findId(temp, id);// find birtdate data for given id number, I don't wanna write new fuction, so I
							// called GetNum1 function, because it's find smaller nodes for given birtdate
							// data.
		System.out.println(GetNum1(temp, findDate[0], findDate[1], findDate[2]));

	}

	public static void main(String[] args) throws IOException {
		RedBlackTree rbt = new RedBlackTree();
		rbt.consoleUI();
	}
}