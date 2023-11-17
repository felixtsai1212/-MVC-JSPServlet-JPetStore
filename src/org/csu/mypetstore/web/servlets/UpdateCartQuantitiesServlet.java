package org.csu.mypetstore.web.servlets;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.persistence.DBUtil;
import org.csu.mypetstore.persistence.ShoppingCartDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class UpdateCartQuantitiesServlet extends HttpServlet {

    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");

        if (cart != null && account != null) {
            try {
                updateCartQuantities(request, cart, account.getUsername());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            session.setAttribute("cart", cart);
        }

        request.getRequestDispatcher(VIEW_CART).forward(request, response);
    }

    private void updateCartQuantities(HttpServletRequest request, Cart cart, String username) throws Exception {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection(); // 获取数据库连接，可以使用连接池或其他方法
            ShoppingCartDAO shoppingCartDAO = new ShoppingCartDAO() {
                @Override
                public void addItemToCart(String username, String itemId, int quantity) {

                }

                @Override
                public void removeItemFromCart(String username, String itemId) {

                }

                @Override
                public void updateItemQuantity(String username, String itemId, int quantity) {

                }

                @Override
                public List<CartItem> getCartItems(String username) {
                    return null;
                }
            };

            Iterator<CartItem> cartItemIterator = cart.getAllCartItems();
            while (cartItemIterator.hasNext()) {
                CartItem cartItem = cartItemIterator.next();
                String itemId = cartItem.getItem().getItemId();
                try {
                    int quantity = Integer.parseInt(request.getParameter(itemId));
                    if (quantity > 0) {
                        cart.setQuantityByItemId(itemId, quantity);
                        shoppingCartDAO.updateItemQuantity(username, itemId, quantity, connection);
                    } else {
                        cartItemIterator.remove();
                        shoppingCartDAO.removeItemFromCart(username, itemId, connection);
                    }
                } catch (NumberFormatException e) {
                    // 适当的异常处理，例如日志记录或向用户显示错误信息
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            // 数据库连接异常处理
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接
            DBUtil.closeConnection(connection);
        }
    }
}