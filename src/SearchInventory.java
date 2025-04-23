import java.io.*;
import java.util.*;

public class SearchInventory {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        List<Product> inventory = getInventory("inventory.csv");

        inventory.sort(Comparator.comparing(Product::getName));

        boolean running = true;
        while (running){
            System.out.println("\nWhat you wanna do?");
            System.out.println("1- List all products");
            System.out.println("2- Lookup product by id");
            System.out.println("3- Find product in price range");
            System.out.println("4- Add a new product");
            System.out.println("5- Quit");
            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    inventory.forEach(System.out::println);
                    break;

                case "2":
                    System.out.print("Enter product ID:");
                    int id = Integer.parseInt(scanner.nextLine());
                    Optional<Product> result = inventory.stream()
                            .filter(p -> p.getId() == id)
                            .findFirst();

                    if (result.isPresent()) {
                        System.out.println(result.get());
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;


                case "3":
                    System.out.print("Enter min price: ");
                    double min = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter max price: ");
                    double max = Double.parseDouble(scanner.nextLine());
                    inventory.stream()
                            .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                            .forEach(System.out:: println);
                    break;


                case "4":
                    System.out.print("Enter product ID: ");
                    int newId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter product name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter product price: ");
                    double newPrice = Double.parseDouble(scanner.nextLine());

                    Product newProduct = new Product(newId, newName, newPrice);
                    inventory.add(newProduct);
                    inventory.sort(Comparator.comparing(Product::getName));
                    System.out.println("Product added.");

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.csv", true))) {
                        writer.newLine();
                        writer.write(String.format("%d|%s|%.2f", newId, newName, newPrice));
                    } catch (IOException e) {
                        System.out.println("Failed to save product to file: " + e.getMessage());
                    }
                    break;

                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again");
            }
        }
        System.out.println("Thanks for using SearchInventory!!!");
    }
    public static List<Product> getInventory (String filename){
        List<Product> inventory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;

            while ((line = reader.readLine()) != null){
                String [] tokens = line.split("\\|");
                int id = Integer.parseInt(tokens[0]);
                String name = tokens[1];
                double price = Double.parseDouble(tokens[2]);

                inventory.add(new Product(id, name, price));
            }
        }catch (IOException e){
            System.out.println("Error loading inventory:" + e.getMessage());
        }
        return inventory;
    }
}