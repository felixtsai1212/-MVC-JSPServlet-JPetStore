package org.csu.mypetstore.persistence.Impl;

import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.persistence.DBUtil;
import org.csu.mypetstore.persistence.ItemDAO;
import org.csu.mypetstore.persistence.ShoppingCartDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAOImpl implements ShoppingCartDAO {

    private Connection getConnection() throws Exception {
        return DBUtil.getConnection();
    }

    private ItemDAO itemDAO;

    public ShoppingCartDAOImpl() {
        this.itemDAO = itemDAO;
    }

    @Override
    public void addItemToCart(String username, String itemId, int quantity) {
        String sql = "INSERT INTO shoppingcart (username, itemid, quantity, date_added) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, itemId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle or log error appropriately
        } catch (Exception e) {
            e.printStackTrace();
            // Handle or log error appropriately
        }
    }

    @Override
    public void removeItemFromCart(String username, String itemId) {
        String sql = "DELETE FROM shoppingcart WHERE username = ? AND itemid = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle or log error appropriately
        } catch (Exception e) {
            e.printStackTrace();
            // Handle or log error appropriately
        }
    }

    @Override
    public void updateItemQuantity(String username, String itemId, int quantity) {
        String sql = "UPDATE shoppingcart SET quantity = ? WHERE username = ? AND itemid = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, username);
            ps.setString(3, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle or log error appropriately
        } catch (Exception e) {
            e.printStackTrace();
            // Handle or log error appropriately
        }
    }

    @Override
    public List<CartItem> getCartItems(String username) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM shoppingcart WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem cartItem = new CartItem();
                    String itemId = rs.getString("itemid");
                    int quantity = rs.getInt("quantity");

                    Item item = itemDAO.getItem(itemId); // 使用ItemDAO获取Item对象
                    cartItem.setItem(item);
                    cartItem.setQuantity(quantity);
                    cartItem.setInStock(itemDAO.getInventoryQuantity(itemId) > 0); // 检查库存

                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle or log error appropriately
        } catch (Exception e) {
            e.printStackTrace();
            // Handle or log error appropriately
        }
        return cartItems;
    }
}

