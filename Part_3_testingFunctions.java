import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
// import java.util.stream.Collectors;

public class Part_3_testingFunctions {
    public static void main(String[] args) {

        LocalDate date = LocalDate.of(2026, 5, 24);// YYYY/MM/DD
        LocalDateTime d = LocalDateTime.now();// YYYY/MM/DD hh:mm
        Duration duration = Duration.between(date.atStartOfDay(), d);// .atStartOfDay() bash nzide heurs o minutes daroriyin f duration

        System.out.println(ChronoUnit.YEARS.between(date, d));
        System.out.println(ChronoUnit.MONTHS.between(date, d));
        System.out.println(ChronoUnit.DAYS.between(date, d));

        System.out.println(duration.getSeconds() + " seconds");
        List<Integer> numbers = List.of(10, 20, 30, 40);

        boolean j = numbers.stream().allMatch(num -> num > 15);// wash kelshi
        boolean k = numbers.stream().anyMatch(num -> num > 15);// wash ghir kayn shi wahed
        boolean x = numbers.stream().noneMatch(s -> s < 0);// Wach ḥtta wahed ma-kber mn 0?
        System.out.println("count: " + numbers.stream().count());
        System.out.println("allMatch >15: " + j);
        System.out.println("anyMatch >15: " + k);
        System.out.println("noneMatch >0: " + x);
        System.out.print("Filtre >15: ");
        numbers.stream()
                .filter(num -> num > 15)
                .forEach(i -> System.out.print(i + "    "));

        System.out.print("\nFiller accending: ");
        numbers.stream().sorted().forEach(num -> System.out.print(num + " "));

        System.out.print("\nFiller diccending: ");
        numbers.stream().sorted(Comparator.reverseOrder()).forEach(num -> System.out.print(num + " "));

        System.out.print("\nList: ");
        numbers.forEach(nu -> System.out.print(nu + "  "));
        System.out.print("\nskip: ");
        numbers.stream().skip(3).forEach(m -> System.out.println(m));

        Optional<Integer> findFirst = numbers.stream().findFirst();// awal wahed men stream
        Optional<Integer> findAny = numbers.stream().findAny();// ayi wahed men stream
        System.out.println("findFirst: " + findFirst + "    findAny: " + findAny);
        System.out.println();

        List<Integer> list = List.of(1, 2, 3, 4, 0, 5, 6, 7, 9);
        System.out.println("List: " + list);
        List<Integer> number = list.stream()
                .filter(nbr -> nbr % 2 == 0)
                .toList(); // collect/toList
        // .collect(Collectors.toList());

        System.out.print("List: " + number + "\nStream: ");

        list.stream().filter(i -> i % 2 == 0).forEach(System.out::print);


        
    }
}
