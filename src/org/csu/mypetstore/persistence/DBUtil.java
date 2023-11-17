package org.csu.mypetstore.persistence;

import java.sql.*;

public class DBUtil {
    private static String driveString = "com.mysql.jdbc.Driver";
    private static String connectionString = "jdbc:mysql://127.0.0.1/mypetstore?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
    private static String username = "root";
    private static String password = "myfelixsql24";

    // 获取连接
    public static Connection getConnection() throws Exception {
        Connection connection = null;

        try {
            Class.forName(driveString);
            connection = DriverManager.getConnection(connectionString, username, password);
        } catch (Exception e) {
            throw e;
        }

        return connection;
    }

    public static void closeStatement(Statement statement) throws Exception {
        if (statement != null) {
            statement.close();
        }
    }

    public static void closePreparedStatement(PreparedStatement pStatement) throws Exception {
        if (pStatement != null) {
            pStatement.close();
        }
    }

    public static void closeResultSet(ResultSet resultSet) throws Exception {
        if (resultSet != null) {
            resultSet.close();
        }
    }

    // 关闭连接
    public static void closeConnection(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    // 添加商品到购物车
    public static void addItemToCart(String username, String itemid, int quantity) throws Exception {
        String sql = "INSERT INTO ShoppingCart (username, itemid, quantity) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, itemid);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    // 更新购物车中的商品数量
    public static void updateCartItem(String username, String itemid, int quantity) throws Exception {
        String sql = "UPDATE ShoppingCart SET quantity = ? WHERE username = ? AND itemid = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, username);
            stmt.setString(3, itemid);
            stmt.executeUpdate();
        }
    }

    // 从购物车中检索商品
    public static ResultSet getCartItems(String username) throws Exception {
        String sql = "SELECT itemid, quantity FROM ShoppingCart WHERE username = ?";
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        return stmt.executeQuery();
        // 注意：调用者负责关闭连接和语句
    }

    // 从购物车中删除商品
    public static void removeCartItem(String username, String itemid) throws Exception {
        String sql = "DELETE FROM ShoppingCart WHERE username = ? AND itemid = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, itemid);
            stmt.executeUpdate();
        }
    }

    public static void closePreparedStatent(PreparedStatement pStatement) {
    }

    public static void closePreparedStatement() {
    }

    /* 测试数据库连接
    public static void main(String[] args) throws Exception {
        Connection conn = DBUtil.getConnection();
        System.out.println(conn);
    }
    */
}
