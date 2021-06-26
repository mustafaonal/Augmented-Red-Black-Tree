package rbt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Augmented_RBT {

	static People[] People = new People[52];
	static Date[] date = new Date[52];
	Node[] node = new Node[52];
	static int x = 0;
	static int number = 0;

	private final int RED = 0;
	private final int BLACK = 1;

	private class Node {

		int id;
		Date birtdate;
		int smallerNodes = 0;// addition

		int key = -1, color = BLACK;
		Node left = nil, right = nil, parent = nil;

		Node(int key) {
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

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public int getSmallerNodes() {
			return smallerNodes;
		}

		public void setSmallerNodes(int smallerNodes) {
			this.smallerNodes = smallerNodes;
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
				+ node.parent.key + "   " + node.getBirtdate().getDay() + "/" + node.getBirtdate().getMounth() + "/"
				+ node.getBirtdate().getYear() + "   " + node.getId() + "  " + node.getSmallerNodes() + "\n");
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
		// all modification is here-------------------------------
		Node temp2 = root;

		inorder(temp2, node);
		node.setSmallerNodes(node.getSmallerNodes() - 1);// I have reduce 1,because my code looking smaller and equal
															// nodes, so it's looking himself and increase 1 more
															// variable
		// ---------------------------------------------------------
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
			Augmented_RBT rbt = new Augmented_RBT();
			switch (choice) {
			case 1:
				readPeople();
				rbt.Insert(rbt);
				System.out.println("all item were inserted.");
				break;
			case 2:
				rbt.Insert(rbt);
				rbt.GETNUMSMALLER1(rbt.root);
				System.out.println(x);
				break;
			case 3:
				rbt.Insert(rbt);
				rbt.GETNUMSMALLER2(rbt.root);
				System.out.println(x);
				break;
			case 4:
				rbt.Insert(rbt);
				rbt.GETMAX(rbt.root);
				break;
			case 5:
				rbt.Insert(rbt);
				rbt.GetMin(rbt.root);
				break;
			case 6:
				rbt.Insert(rbt);
				System.out.println("The number of all people is " + rbt.GetNum(rbt.root));
				number = 0;
				break;
			case 7:
				readPeople();
				rbt.Insert(rbt);
				rbt.printTree(rbt.root);
				break;
			}
		}
	}

	static void readPeople() throws IOException {

		FileInputStream fileRead = new FileInputStream("people.txt");
		DataInputStream data = new DataInputStream(fileRead);
		BufferedReader read = new BufferedReader(new InputStreamReader(data));
		int numOfPeople = 0;

		String line;
		int id = 0, day = 0, month = 0, year = 0;
		String[] inputs = null;
		String[] date_inputs = null;

		while ((line = read.readLine()) != null) {
			// Every line of the file was assigned to 'line' at every turn.
			inputs = line.split(",");

			for (int i = 0; i < inputs.length; i++) {
				switch (i) {
				case 0:
					id = Integer.parseInt(inputs[i]);
					break;
				case 1:
					date_inputs = inputs[i].split("/");
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

	// this is for hold the number of smaller nodes for each
	// nodes----------------------------------

	public void inorder(Node rbt_Root, Node node) {
		Node temp = rbt_Root;// this function basically inorder recursive function, temp is root which is
								// tree
		if (temp.left != null) {
			inorder(temp.left, node);// going most left first
			if (temp.getKey() <= node.getKey())// if node which inserting element if bigger then temp, increase node's
												// number, because this see us we found node which smaller then
												// inserting element
				node.setSmallerNodes(node.getSmallerNodes() + 1);
			else
				temp.setSmallerNodes(temp.getSmallerNodes() + 1);// this is for all bigger nodes then inserting element,
																	// increase all their number 1.

			inorder(temp.right, node);
		}
	}

	// -----------------------------------------------------------------------------------------------

	public void Insert(Augmented_RBT rbt) throws IOException {

		for (int i = 0; i < People.length; i++) {
			node[i] = new Node(People[i].getAge());
			node[i].setKey(People[i].getAge());
			node[i].setBirtdate(People[i].getBirtdate());
			node[i].setId(People[i].getId());
			rbt.insert(node[i]);
		}

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

		findbyBirtdate(temp, day, month, year);

	}

	public void findbyBirtdate(Node node, int day, int month, int year) {
		Node temp = node;
		if (temp.left != null) {
			findbyBirtdate(temp.left, day, month, year);
			if (temp.getBirtdate().getYear() == year && temp.getBirtdate().getMounth() == month
					&& temp.getBirtdate().getDay() == day) {
				x = temp.getSmallerNodes();
			}
			findbyBirtdate(temp.right, day, month, year);

		}

	}

	public void GETNUMSMALLER2(Node node) {
		Node temp = node;
		Scanner input = new Scanner(System.in);
		int id;
		System.out.print("Please enter a id: ");
		id = input.nextInt();

		findbyID(temp, id);
	}

	public void findbyID(Node node, int id) {
		Node temp = node;
		if (temp.left != null) {
			findbyID(temp.left, id);
			if (temp.getId() == id)
				x = temp.getSmallerNodes();

			findbyID(temp.right, id);
		}
	}

	public void GETMAX(Node node) {
		Node temp = node;
		int max;
		int id;
		Date birtdate;
		do {
			max = temp.getKey();
			id = temp.getId();
			birtdate = temp.getBirtdate();
			temp = temp.right;
		} while (temp.right != null);

		System.out.print("The maximum age of all people is " + max + "  id: " + id + "  Birtdate: " + birtdate.getDay()
				+ "/" + birtdate.getMounth() + "/" + birtdate.getYear());
		System.out.println();

	}

	public void GetMin(Node node) {
		Node temp = node;
		int min;
		int id;
		Date birtdate;
		do {
			min = temp.getKey();
			id = temp.getId();
			birtdate = temp.getBirtdate();
			temp = temp.left;
		} while (temp.left != null);

		System.out.print("The minumum age of all people is " + min + "  id: " + id + "  Birtdate: " + birtdate.getDay()
				+ "/" + birtdate.getMounth() + "/" + birtdate.getYear());
		System.out.println();

	}

	public int GetNum(Node node) {
		Node temp = node;
		if (temp.left != null) {
			GetNum(temp.left);
			number++;
			GetNum(temp.right);
		}
		return number;

	}

	public static void main(String[] args) throws IOException {
		Augmented_RBT rbt = new Augmented_RBT();
		rbt.consoleUI();
	}
}