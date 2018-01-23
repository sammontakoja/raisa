import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class PhoneBook {

    public static void main(String[] args) {
        new PhoneBook().use();
    }

    PhoneBook() {
        System.out.println("Welcome to PhoneBook!");
        new SQL().execute("CREATE TABLE IF NOT EXISTS phone_book(name VARCHAR(255), number VARCHAR(255))");
    }

    void use() {

        System.out.println("1 => New number, 2 => All numbers, 3 => Numbers by name, 4 => Remove number(s), 5 => Quit");

        switch (new Input().line()) {
            case "1":
                new SQL().execute("insert into phone_book values('" + new Input().name() + "', '" + new Input().number() + "')");
                break;
            case "2":
                new SQL().print("select * from phone_book");
                break;
            case "3":
                new SQL().print("select number from phone_book where name='" + new Input().name() + "'");
                break;
            case "4":
                new SQL().execute("delete from phone_book where name='" + new Input().name() + "'");
                break;
            case "5":
                System.exit(0);
                break;
        }

        use();
    }
}

class Input {

    String line() {
        return new Scanner(System.in).nextLine();
    }

    String name() {
        System.out.println("Please type name..");
        return new Scanner(System.in).nextLine();
    }

    String number() {
        System.out.println("Please type phone number..");
        return new Scanner(System.in).nextLine();
    }
}

class SQL {

    void execute(String query) {
        jdbcConnection(connection -> connection.prepareStatement(query).execute());
    }

    void print(String query) {

        jdbcConnection(connection -> {

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1)
                        System.out.print(" | ");
                    System.out.print(rs.getString(i));
                }
                System.out.println("");
            }

        });
    }

    private void jdbcConnection(Statement statement) {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");

            statement.execute(conn);

            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private interface Statement {
        void execute(Connection conn) throws Exception;
    }
}


