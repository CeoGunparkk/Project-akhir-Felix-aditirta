import java.util.*;
import java.util.regex.Pattern;

class Employee {
    String code, name, gender, position;
    double salary;
    Date created;

    Employee(String code, String name, String gender, String position, double salary) {
        this.code = code;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.salary = salary;
        this.created = new Date();
    }
}

public class EmployeeManagement {
    static Scanner in = new Scanner(System.in);
    static List<Employee> list = new ArrayList<>();
    static Pattern codePat = Pattern.compile("[A-Z]{2}-\\d{4}");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1.Insert 2.View 3.Update 4.Delete 5.Exit");
            switch (in.nextLine()) {
                case "1": insert(); break;
                case "2": view(); break;
                case "3": update(); break;
                case "4": delete(); break;
                case "5": return;
                default: System.out.println("Invalid");
            }
        }
    }

    static void insert() {
        String code = read("Code (AA-1234)", codePat);
        String name = read("Name (min 3 letters)", Pattern.compile("[A-Za-z ]{3,}"));
        String gender = choose("Gender", "Laki-Laki", "Perempuan");
        String pos = choose("Position", "Manager", "Supervisor", "Admin");
        double base = pos.equals("Manager") ? 8e6 : pos.equals("Supervisor") ? 6e6 : 4e6;
        list.add(new Employee(code, name, gender, pos, base));
        bonus();
        System.out.println("Added.");
    }

    static void view() {
        if (list.isEmpty()) { System.out.println("No data."); return; }
        list.stream()
            .sorted(Comparator.comparing(e -> e.name))
            .forEach(e -> System.out.printf("%s | %s | %s | %s | %.0f%n", e.code, e.name, e.gender, e.position, e.salary));
    }

    static void update() {
        if (list.isEmpty()) { System.out.println("No data."); return; }
        view(); System.out.print("Pick idx: ");
        int i = Integer.parseInt(in.nextLine()) - 1;
        if (i<0||i>=list.size()) { System.out.println("Invalid"); return; }
        list.remove(i);
        insert();
        System.out.println("Updated.");
    }

    static void delete() {
        if (list.isEmpty()) { System.out.println("No data."); return; }
        view(); System.out.print("Pick idx: ");
        int i = Integer.parseInt(in.nextLine()) - 1;
        if (i<0||i>=list.size()) { System.out.println("Invalid"); return; }
        list.remove(i);
        bonus();
        System.out.println("Deleted.");
    }

    static String read(String prompt, Pattern p) {
        while (true) {
            System.out.print(prompt+": ");
            String s = in.nextLine();
            if (p.matcher(s).matches()) return s;
            System.out.println("Wrong format.");
        }
    }

    static String choose(String field, String... opts) {
        while (true) {
            System.out.print(field+" "+Arrays.toString(opts)+": ");
            String s = in.nextLine();
            for (String o:opts) if (o.equals(s)) return s;
            System.out.println("Choose one.");
        }
    }

    static void bonus() {
        Map<String, Double> rate = Map.of("Manager", .10, "Supervisor", .075, "Admin", .05);
        for (String p: rate.keySet()) {
            List<Employee> sub = new ArrayList<>();
            for (Employee e:list) if (e.position.equals(p)) sub.add(e);
            sub.sort(Comparator.comparing(e->e.created));
            int cnt = (sub.size()/3)*3;
            for (int i=0;i<sub.size();i++) {
                sub.get(i).salary = sub.get(i).salary / (1+rate.get(p));
                if (i<cnt) sub.get(i).salary *= (1+rate.get(p));
            }
        }
    }
}
