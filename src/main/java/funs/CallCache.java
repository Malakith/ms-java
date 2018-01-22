package funs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CallCache<T> {
    static final String WRITE_OBJECT_SQL =  "INSERT OR IGNORE INTO java_objects VALUES (?,?);";
    static final String UPDATE_OBJECT_SQL = "UPDATE java_objects SET object_value = ? WHERE name LIKE ?;";
    static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE name = ?";

    private Connection db;

    public CallCache() {
        db = createDatabase("jdbc:sqlite::memory:");
    }

    public CallCache(String filename) {
        db = createDatabase("jdbc:sqlite:" + filename + ".sqlite");
    }

    private Connection createDatabase(String url) {
        Connection result = null;
        try {
            result = DriverManager.getConnection(url);
            Statement stm = result.createStatement();
            stm.execute("create table IF NOT EXISTS java_objects (" +
                    "name text," +
                    "object_value blob," +
                    "primary key (name)" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(String id, T response) {
        try {
            PreparedStatement stm = db.prepareStatement(WRITE_OBJECT_SQL);
            stm.setString(1, id);
            stm.setObject(2, response);

            stm.executeUpdate();
            stm.close();
            stm = db.prepareStatement(UPDATE_OBJECT_SQL);
            stm.setObject(1, response);
            stm.setString(2, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(String id, List<T> response) {
        System.out.println("Called with list");
        for (T t : response) {
            save(id, t);
        }

    }

    public List<T> load(String id) {
        List<T> response = new ArrayList<>();
        try {
            PreparedStatement stm = db.prepareStatement(READ_OBJECT_SQL);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();


            while (rs.next()) {
                response.add((T) rs.getObject(1));
            }
            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (response.size() == 0) {
            return null;
        } else {
            return response;
        }
    }
}
