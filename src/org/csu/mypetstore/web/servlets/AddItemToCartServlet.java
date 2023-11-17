package org.csu.mypetstore.web.servlets;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.persistence.Impl.ShoppingCartDAOImpl;
import org.csu.mypetstore.service.CatalogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AddItemToCartServlet extends HttpServlet {
    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";

    private ShoppingCartDAOImpl shoppingCartDAO;
    private CatalogService catalogService;

    public AddItemToCartServlet() {
        // 这里需要提供ItemDAO的真实实现，以便ShoppingCartDAOImpl可以正确工作
        // 请根据您的项目配置来调整这里的代码
        this.shoppingCartDAO = new ShoppingCartDAOImpl();
        this.catalogService = new CatalogService(); // 确保CatalogService已经被正确配置
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workingItemId = request.getParameter("workingItemId");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");

        if (cart == null) {
            cart = new Cart();
        }

        try {
            updateCart(workingItemId, cart, account);
        } catch (Exception e) {
            e.printStackTrace();
            // 这里可以添加更多的错误处理逻辑
        }

        session.setAttribute("cart", cart);
        request.getRequestDispatcher(VIEW_CART).forward(request, response);
    }

    private void updateCart(String itemId, Cart cart, Account account) throws Exception {
        if (cart.containsItemId(itemId)) {
            cart.incrementQuantityByItemId(itemId);
            if (account != null) {
                shoppingCartDAO.updateItemQuantity(account.getUsername(), itemId, cart.getQuantityByItemId(itemId));
            }
        } else {
            boolean isInStock = catalogService.isItemInStock(itemId);
            Item item = catalogService.getItem(itemId);
            if (item != null) {
                cart.addItem(item, isInStock);
                if (account != null) {
                    shoppingCartDAO.addItemToCart(account.getUsername(), itemId, 1);
                }
            }
        }
    }
}

