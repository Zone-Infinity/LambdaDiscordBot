package Test;

import java.util.List;

class Person {
    public String name;
    public int points;

    public Person(String name, int points) {
        this.name = name;
        this.points = points;
    }
}

public class TestClass {
    public static void main(String[] args) {
        List<Person> persons = new java.util.ArrayList<>(List.of(new Person("Zone-Infinity", 10), new Person("Aalbatross Guy", 1), new Person("Loading BG", 14), new Person("JON", 5), new Person("Duncte123", 17)));
        final int namesSize = persons.stream().mapToInt(it -> it.name.length()).max().orElse(0);
        final int pointSize = persons.stream().mapToInt(it -> String.valueOf(it.points).length()).max().orElse(0);
        String rowFormat = "║%-" + (Math.max(5, String.valueOf(persons.size()).length()) + 1) + "s║%-" + (Math.max(namesSize, 5) + 1) + "s║%" + (Math.max(pointSize, 7) + 1) + "s║%n";

        final String divider = String.format(rowFormat, "", "", "").replaceAll(" ", "═");

        System.out.print(String.format(rowFormat, "", "", "").replaceFirst("║", "╔").replaceFirst("║", "╦").replaceFirst("║", "╦").replaceFirst("║", "╗").replaceAll(" ", "═"));
        System.out.printf(rowFormat, "Rank ", "Names", "Points ");
        System.out.print(divider);

        persons.sort((p1, p2) -> Integer.compare(p2.points, p1.points));

        for (int i = 0; i < persons.size(); i++) {
            System.out.printf(rowFormat, (i + 1) + ".", persons.get(i).name, persons.get(i).points);
        }

        System.out.print(String.format(rowFormat, "", "", "").replaceFirst("║", "╚").replaceFirst("║", "╩").replaceFirst("║", "╩").replaceFirst("║", "╝").replaceAll(" ", "═"));
    }
}
