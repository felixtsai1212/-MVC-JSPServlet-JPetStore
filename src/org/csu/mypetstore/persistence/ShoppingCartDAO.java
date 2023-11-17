package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ShoppingCartDAO {
    static final String ADD_ITEM_SQL = "INSERT INTO shoppingcart (username, itemid, quantity) VALUES (?, ?, ?)";
    static final String UPDATE_ITEM_QUANTITY_SQL = "UPDATE shoppingcart SET quantity = ? WHERE username = ? AND itemid = ?";
    static final String REMOVE_ITEM_SQL = "DELETE FROM shoppingcart WHERE username = ? AND itemid = ?";

    public default void addItemToCart(String username, String itemId, int quantity, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_ITEM_SQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, itemId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();
        }
    }

    public default void updateItemQuantity(String username, String itemId, int quantity, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ITEM_QUANTITY_SQL)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, itemId);
            preparedStatement.executeUpdate();
        }
    }

    public default void removeItemFromCart(String username, String itemId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_ITEM_SQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, itemId);
            preparedStatement.executeUpdate();
        }
    }

    public abstract void addItemToCart(String username, String itemId, int quantity);

    public abstract void removeItemFromCart(String username, String itemId);

    public abstract void updateItemQuantity(String username, String itemId, int quantity);

    public abstract List<CartItem> getCartItems(String username);

    // 其他数据库操作方法可以根据需要添加
}



