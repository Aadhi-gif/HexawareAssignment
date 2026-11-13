class Node {
    String data;
    Node left, right;

    Node(String data) {
        this.data = data;
    }
}

class Task1_BST {

    Node root;

    void insert(String value) {
        root = add(root, value);
    }

    Node add(Node root, String value) {

        if (root == null)
            return new Node(value);

        if (value.compareTo(root.data) < 0)
            root.left = add(root.left, value);
        else if (value.compareTo(root.data) > 0)
            root.right = add(root.right, value);

        return root;
    }

    void inorder(Node root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.data + " ");
            inorder(root.right);
        }
    }

    void preorder(Node root) {
        if (root != null) {
            System.out.print(root.data + " ");
            preorder(root.left);
            preorder(root.right);
        }
    }

    void postorder(Node root) {
        if (root != null) {
            postorder(root.left);
            postorder(root.right);
            System.out.print(root.data + " ");
        }
    }

    void search(String value) {
        Node temp = root;

        while (temp != null) {
            if (temp.data.equals(value)) {
                System.out.println(value + " Found");
                return;
            }

            if (value.compareTo(temp.data) < 0)
                temp = temp.left;
            else
                temp = temp.right;
        }

        System.out.println(value + " Not Found");
    }

    public static void main(String[] args) {

        Task1_BST tree = new Task1_BST();

        String[] categories = {
                "Electronics", "Clothing", "Books",
                "Furniture", "Mobiles", "Laptops",
                "Shoes", "Watches", "Groceries",
                "Beauty", "Sports", "Toys",
                "Kitchen", "Jewellery", "Accessories"
        };

        for (String s : categories)
            tree.insert(s);

        System.out.println("Inorder:");
        tree.inorder(tree.root);

        System.out.println("\nPreorder:");
        tree.preorder(tree.root);

        System.out.println("\nPostorder:");
        tree.postorder(tree.root);

        System.out.println("\nSearch:");
        tree.search("Mobiles");
        tree.search("Camera");
    }
}